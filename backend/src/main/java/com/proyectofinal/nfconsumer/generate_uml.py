import os
import re
import subprocess
from collections import defaultdict

PROJECT_ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../../.."))
INPUT_DIR = "."
OUTPUT_FILE = os.path.join(PROJECT_ROOT, "resources/diagrams/diagrama-clases.puml")
OUTPUT_IMAGE = os.path.join(PROJECT_ROOT, "resources/diagrams/diagrama-clases.png")

def parse_java_class(file_path, file_content):
    class_match = re.search(r'(class|record|interface)\s+(\w+)', file_content)
    if not class_match:
        return None

    class_type = class_match.group(1)
    class_name = class_match.group(2)

    annotations = re.findall(r'@(\w+)', file_content)

    fields = re.findall(r'(private|protected|public)\s+([\w<>\.]+)\s+(\w+);', file_content)
    methods = re.findall(r'(public|protected|private)\s+([\w<>\.]+)\s+(\w+)\s*\(', file_content)

    extends_match = re.search(r'extends\s+(\w+)', file_content)
    implements_match = re.findall(r'implements\s+([\w,\s]+)', file_content)

    has_main = bool(re.search(r'public\s+static\s+void\s+main\s*\(String\[\]\s+\w+\)', file_content))

    extends = extends_match.group(1) if extends_match else None
    implements = []
    if implements_match:
        for impl in implements_match:
            implements += [i.strip() for i in impl.split(',')]

    relative_path = os.path.relpath(file_path, INPUT_DIR)
    parts = relative_path.split(os.sep)[:-1]
    package = '.'.join(parts) if parts else "default"

    return {
        "name": class_name,
        "type": class_type,
        "annotations": annotations,
        "fields": fields,
        "methods": methods,
        "extends": extends,
        "implements": implements,
        "package": package,
        "has_main": has_main
    }

def generate_puml(classes):
    lines = ["@startuml"]

    grouped = defaultdict(list)
    for cls in classes:
        grouped[cls['package']].append(cls)

    for package, class_list in grouped.items():
        lines.append(f"package {package} {{")
        for cls in class_list:
            stereotype = "interface" if cls['type'] == "interface" else "class"
            annotation_tags = [a for a in cls['annotations'] if a in ['Service', 'RestController', 'Component', 'Entity']]
            if cls['has_main']:
                annotation_tags.append("Main")
            annotation_str = f" <<{', '.join(annotation_tags)}>>" if annotation_tags else ""
            lines.append(f"  {stereotype} {cls['name']}{annotation_str} {{")
            for access, type_, name in cls['fields']:
                symbol = '+' if access == 'public' else '-' if access == 'private' else '#'
                lines.append(f"    {symbol} {name} : {type_}")
            for access, ret, name in cls['methods']:
                symbol = '+' if access == 'public' else '-' if access == 'private' else '#'
                lines.append(f"    {symbol} {name}() : {ret}")
            lines.append("  }")
        lines.append("}")

    relation_set = set()

    for cls in classes:
        for _, type_, _ in cls['fields']:
            for other in classes:
                if type_ == other['name'] and cls['name'] != other['name']:
                    relation_set.add(f"{cls['name']} --> {other['name']}")

        for _, ret, _ in cls['methods']:
            for other in classes:
                if ret == other['name'] and cls['name'] != other['name']:
                    relation_set.add(f"{cls['name']} ..> {other['name']}")

    for cls in classes:
        if cls['extends']:
            relation_set.add(f"{cls['extends']} <|-- {cls['name']}")
        for interface in cls['implements']:
            relation_set.add(f"{interface} <|.. {cls['name']}")

    lines.extend(sorted(relation_set))
    lines.append("@enduml")
    return "\n".join(lines)

def render_image(input_file, output_file):
    try:
        subprocess.run(["plantuml", "-tpng", "-o", os.path.dirname(output_file), input_file], check=True)
        print(f"Imagen generada: {output_file}")
    except Exception as e:
        print(f"Error al generar imagen PNG: {e}")

def main():
    java_classes = []
    total_classes = 0
    total_interfaces = 0
    total_relations = 0
    total_mains = 0

    for root, _, files in os.walk(INPUT_DIR):
        for file in files:
            if file.endswith(".java"):
                full_path = os.path.join(root, file)
                with open(full_path, "r", encoding="utf-8") as f:
                    content = f.read()
                    parsed = parse_java_class(full_path, content)
                    if parsed:
                        java_classes.append(parsed)
                        if parsed['type'] == 'interface':
                            total_interfaces += 1
                        else:
                            total_classes += 1
                        if parsed['has_main']:
                            total_mains += 1

    puml_output = generate_puml(java_classes)
    total_relations = puml_output.count(" --> ") + puml_output.count(" <|-- ") + puml_output.count(" <|.. ") + puml_output.count(" ..> ")

    os.makedirs(os.path.dirname(OUTPUT_FILE), exist_ok=True)
    with open(OUTPUT_FILE, "w", encoding="utf-8") as f:
        f.write(puml_output)

    print(f"Archivo generado: {OUTPUT_FILE}")
    print(f"Clases: {total_classes}, Interfaces: {total_interfaces}, Métodos main: {total_mains}, Relaciones: {total_relations}")

    render_image(OUTPUT_FILE, OUTPUT_IMAGE)

if __name__ == "__main__":
    main()
