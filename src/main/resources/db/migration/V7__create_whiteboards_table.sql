CREATE TABLE whiteboards (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    preview_image_url VARCHAR(255),

    FOREIGN KEY (user_id) REFERENCES users(id)
)