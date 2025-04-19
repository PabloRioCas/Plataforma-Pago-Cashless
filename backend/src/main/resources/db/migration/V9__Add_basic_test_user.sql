INSERT INTO users (
    password,
    name,
    lastname,
    email,
    phone
) VALUES (
    '$2a$10$YrB/hh2IzTT5IK.IOwye1Obzcd6cnzwnWi8Vrqvhx4flmqNFcBTa2',
    'usuario1',
    'apellido1',
    'usuario1@example.com',
    '123456789'
);

INSERT INTO tokens (
    token,
    token_type,
    is_revoked,
    is_expired,
    user_id
) VALUES (
    'eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoidXN1YXJpbzEiLCJzdWIiOiJ1c3VhcmlvMUBleGFtcGxlLmNvbSIsImlhdCI6MTc0NTAxNzE1MywiZXhwIjoxNzQ1NjIxOTUzfQ.IcZ_cGrMNpPLVrWtrlXP5dxRCd2UieKnMwV9Yhil140',
    'BEARER',
    false,
    false,
    1
);
