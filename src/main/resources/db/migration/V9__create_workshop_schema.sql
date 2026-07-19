-- =========================================================
-- V9: Workshop schema (picture frames production)
-- Stock/total arithmetic is handled in the application layer
-- (WorkshopService classes, @Transactional), not via DB triggers.
-- =========================================================

CREATE TABLE picture_frames
(
    frame_id              BIGSERIAL PRIMARY KEY,
    width_cm              NUMERIC(6, 2)  NOT NULL,
    height_cm             NUMERIC(6, 2)  NOT NULL,
    dimension_description VARCHAR(50)    NOT NULL,
    price                 NUMERIC(10, 2) NOT NULL CHECK (price >= 0),
    active                BOOLEAN        NOT NULL DEFAULT TRUE
);

CREATE TABLE materials
(
    material_id     BIGSERIAL PRIMARY KEY,
    name            VARCHAR(100)   NOT NULL,
    type            VARCHAR(50)    NOT NULL,
    dimensions      VARCHAR(50),
    unit_of_measure VARCHAR(20)    NOT NULL,
    min_stock       NUMERIC(10, 2) NOT NULL DEFAULT 0,
    current_stock   NUMERIC(10, 2) NOT NULL DEFAULT 0
);

CREATE TABLE batch_recipes
(
    recipe_id      BIGSERIAL PRIMARY KEY,
    frame_id       BIGINT NOT NULL REFERENCES picture_frames (frame_id),
    batch_quantity INT    NOT NULL CHECK (batch_quantity > 0),
    description    VARCHAR(200)
);

CREATE TABLE recipe_details
(
    recipe_detail_id  BIGSERIAL PRIMARY KEY,
    recipe_id         BIGINT         NOT NULL REFERENCES batch_recipes (recipe_id) ON DELETE CASCADE,
    material_id       BIGINT         NOT NULL REFERENCES materials (material_id),
    required_quantity NUMERIC(10, 2) NOT NULL CHECK (required_quantity > 0),
    UNIQUE (recipe_id, material_id)
);

CREATE TABLE production_orders
(
    order_id           BIGSERIAL PRIMARY KEY,
    frame_id           BIGINT      NOT NULL REFERENCES picture_frames (frame_id),
    recipe_id          BIGINT      NOT NULL REFERENCES batch_recipes (recipe_id),
    requested_quantity INT         NOT NULL CHECK (requested_quantity > 0),
    start_date         DATE        NOT NULL,
    end_date           DATE,
    status             VARCHAR(20) NOT NULL DEFAULT 'in_progress'
        CHECK (status IN ('in_progress', 'completed', 'canceled')),
    CHECK (end_date IS NULL OR end_date >= start_date)
);

CREATE TABLE frame_deliveries
(
    delivery_id        BIGSERIAL PRIMARY KEY,
    order_id           BIGINT NOT NULL REFERENCES production_orders (order_id),
    delivery_date       DATE   NOT NULL,
    quantity_delivered INT    NOT NULL CHECK (quantity_delivered > 0),
    remarks            VARCHAR(200)
);

CREATE TABLE purchases
(
    purchase_id   BIGSERIAL PRIMARY KEY,
    purchase_date DATE           NOT NULL,
    supplier      VARCHAR(100),
    total         NUMERIC(12, 2) NOT NULL DEFAULT 0
);

CREATE TABLE purchase_details
(
    purchase_detail_id BIGSERIAL PRIMARY KEY,
    purchase_id        BIGINT         NOT NULL REFERENCES purchases (purchase_id) ON DELETE CASCADE,
    material_id        BIGINT         NOT NULL REFERENCES materials (material_id),
    quantity_purchased NUMERIC(10, 2) NOT NULL CHECK (quantity_purchased > 0),
    unit_price         NUMERIC(10, 2) NOT NULL CHECK (unit_price >= 0),
    subtotal           NUMERIC(12, 2) NOT NULL
);

CREATE TABLE material_usage
(
    usage_id      BIGSERIAL PRIMARY KEY,
    order_id      BIGINT         NOT NULL REFERENCES production_orders (order_id),
    material_id   BIGINT         NOT NULL REFERENCES materials (material_id),
    usage_date    DATE           NOT NULL,
    quantity_used NUMERIC(10, 2) NOT NULL CHECK (quantity_used > 0)
);
