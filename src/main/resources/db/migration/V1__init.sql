-- ======================
-- PostgreSQL schema init
-- ======================

CREATE TABLE books (
    id BIGSERIAL PRIMARY KEY,
    isbn VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL
);

CREATE TABLE borrowers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE borrow_records (
    id BIGSERIAL PRIMARY KEY,
    borrower_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    borrowed_at TIMESTAMP NOT NULL,
    returned_at TIMESTAMP,
    CONSTRAINT fk_borrower FOREIGN KEY (borrower_id) REFERENCES borrowers(id),
    CONSTRAINT fk_book FOREIGN KEY (book_id) REFERENCES books(id)
);

-- ======================
-- Sample Data
-- ======================

INSERT INTO borrowers (name, email) VALUES
('John Doe', 'john.doe@email.com'),
('Jane Smith', 'jane.smith@email.com'),
('Bob Johnson', 'bob.johnson@email.com');

INSERT INTO books (isbn, title, author) VALUES
('978-0-123456-47-2', 'The Great Gatsby', 'F. Scott Fitzgerald'),
('978-0-123456-48-9', 'To Kill a Mockingbird', 'Harper Lee'),
('978-0-123456-49-6', '1984', 'George Orwell'),
('978-0-123456-50-2', 'Pride and Prejudice', 'Jane Austen');
