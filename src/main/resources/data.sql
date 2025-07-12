-- Insert test users
INSERT INTO users (username, password, email, role) VALUES
('admin', 'admin123', 'admin@bookingsystem.com', 'ADMIN'),
('user', 'user123', 'user@bookingsystem.com', 'USER'),
('juan', 'juan123', 'juan@email.com', 'USER');

-- Insert test reservations
INSERT INTO reservations (customer_name, customer_email, start_time, end_time, description, status, user_id) VALUES
('Juan Perez', 'juan@email.com', '2025-07-15 10:00:00', '2025-07-15 11:00:00', 'Consulta médica', 'ACTIVE', 2),
('Maria Garcia', 'maria@email.com', '2025-07-15 14:00:00', '2025-07-15 15:00:00', 'Revisión dental', 'ACTIVE', 3),
('Carlos Lopez', 'carlos@email.com', '2025-07-16 09:00:00', '2025-07-16 10:00:00', 'Consulta general', 'ACTIVE', 2);