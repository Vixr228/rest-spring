CREATE TABLE UserEntity (
    id SERIAL PRIMARY KEY,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50),
    email VARCHAR(100) NOT NULL
);

CREATE TABLE Car (
     id SERIAL PRIMARY KEY,
     name VARCHAR(100) NOT NULL,
     user_id INT NOT NULL,
     price DECIMAL(10, 2) NOT NULL,
     FOREIGN KEY (user_id) REFERENCES UserEntity(id)
);