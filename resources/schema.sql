DROP TABLE IF EXISTS recurring_payments;
DROP TABLE IF EXISTS merchants;
DROP TABLE IF EXISTS installments;
DROP TABLE IF EXISTS business_loans;
DROP TABLE IF EXISTS personal_loans;
DROP TABLE IF EXISTS loans;
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS current_accounts;
DROP TABLE IF EXISTS savings_accounts;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS individual_clients;
DROP TABLE IF EXISTS business_clients;
DROP TABLE IF EXISTS clients;

CREATE TABLE clients (
    -- id VARCHAR(50) PRIMARY KEY,
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    client_type VARCHAR(20) NOT NULL,
    address VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    CONSTRAINT chk_clients_type
        CHECK (client_type IN ('INDIVIDUAL', 'BUSINESS'))
);

CREATE TABLE individual_clients (
    -- client_id VARCHAR(50) PRIMARY KEY,
    client_id BIGINT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    cnp VARCHAR(20) NOT NULL UNIQUE,
    date_of_birth VARCHAR(20) NOT NULL,
    occupation VARCHAR(100),
    monthly_income DECIMAL(15, 2),
    CONSTRAINT fk_individual_clients_clients
        FOREIGN KEY (client_id) REFERENCES clients(id)
        ON DELETE CASCADE
);

CREATE TABLE business_clients (
    -- client_id VARCHAR(50) PRIMARY KEY,
    client_id BIGINT PRIMARY KEY,
    company_name VARCHAR(150) NOT NULL,
    cui VARCHAR(30) NOT NULL UNIQUE,
    contact_person VARCHAR(100) NOT NULL,
    monthly_revenue DECIMAL(15, 2),
    monthly_expenses DECIMAL(15, 2),
    CONSTRAINT fk_business_clients_clients
        FOREIGN KEY (client_id) REFERENCES clients(id)
        ON DELETE CASCADE
);

CREATE TABLE accounts (
    -- id VARCHAR(50) PRIMARY KEY,
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    iban VARCHAR(100) NOT NULL UNIQUE,
    account_type VARCHAR(20) NOT NULL,
    opened_date DATE NOT NULL,
    currency VARCHAR(10) NOT NULL,
    balance DECIMAL(15, 2) NOT NULL,
    -- client_id VARCHAR(50) NOT NULL,
    client_id BIGINT NOT NULL,
    CONSTRAINT chk_accounts_type
        CHECK (account_type IN ('CURRENT', 'SAVINGS')),
    CONSTRAINT chk_accounts_currency
        CHECK (currency IN ('RON', 'EUR')),
    CONSTRAINT fk_accounts_clients
        FOREIGN KEY (client_id) REFERENCES clients(id)
        ON DELETE CASCADE
);

CREATE TABLE current_accounts (
    -- account_id VARCHAR(50) PRIMARY KEY,
    account_id BIGINT PRIMARY KEY,
    monthly_fee DECIMAL(15, 2) NOT NULL,
    CONSTRAINT fk_current_accounts_accounts
        FOREIGN KEY (account_id) REFERENCES accounts(id)
        ON DELETE CASCADE
);

CREATE TABLE savings_accounts (
    -- account_id VARCHAR(50) PRIMARY KEY,
    account_id BIGINT PRIMARY KEY,
    interest_rate DECIMAL(10, 4) NOT NULL,
    minimum_balance DECIMAL(15, 2) NOT NULL,
    withdrawal_limit DECIMAL(15, 2) NOT NULL,
    CONSTRAINT fk_savings_accounts_accounts
        FOREIGN KEY (account_id) REFERENCES accounts(id)
        ON DELETE CASCADE
);

CREATE TABLE users (
    -- id VARCHAR(50) PRIMARY KEY,
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(30) NOT NULL,
    -- client_id VARCHAR(50) NOT NULL UNIQUE,
    client_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_users_clients
        FOREIGN KEY (client_id) REFERENCES clients(id)
        ON DELETE CASCADE
);

CREATE TABLE transactions (
    -- id VARCHAR(50) PRIMARY KEY,
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    source_iban VARCHAR(100),
    destination_iban VARCHAR(100),
    amount DECIMAL(15, 2) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    transaction_type VARCHAR(30) NOT NULL,
    timestamp DATETIME NOT NULL,
    description VARCHAR(255),
    CONSTRAINT chk_transactions_currency
        CHECK (currency IN ('RON', 'EUR')),
    CONSTRAINT chk_transactions_type
        CHECK (transaction_type IN (
            'DEPOSIT',
            'WITHDRAW',
            'TRANSFER',
            'LOAN_DISBURSEMENT',
            'LOAN_PAYMENT',
            'RECURRING_PAYMENT'
        )),
    CONSTRAINT fk_transactions_source_account
        FOREIGN KEY (source_iban) REFERENCES accounts(iban)
        ON DELETE SET NULL,
    CONSTRAINT fk_transactions_destination_account
        FOREIGN KEY (destination_iban) REFERENCES accounts(iban)
        ON DELETE SET NULL
);

CREATE TABLE loans (
    -- id VARCHAR(50) PRIMARY KEY,
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    loan_number VARCHAR(50) NOT NULL UNIQUE,
    -- client_id VARCHAR(50) NOT NULL,
    client_id BIGINT NOT NULL,
    linked_account_iban VARCHAR(100) NOT NULL,
    loan_type VARCHAR(20) NOT NULL,
    requested_amount DECIMAL(15, 2) NOT NULL,
    interest_rate DECIMAL(10, 4) NOT NULL,
    number_of_months INT NOT NULL,
    frequency VARCHAR(20) NOT NULL,
    remaining_amount DECIMAL(15, 2) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT chk_loans_type
        CHECK (loan_type IN ('PERSONAL', 'BUSINESS')),
    CONSTRAINT chk_loans_frequency
        CHECK (frequency IN ('WEEKLY', 'MONTHLY', 'YEARLY')),
    CONSTRAINT chk_loans_status
        CHECK (status IN ('PENDING', 'APPROVED', 'ACTIVE', 'REJECTED', 'CLOSED')),
    CONSTRAINT fk_loans_clients
        FOREIGN KEY (client_id) REFERENCES clients(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_loans_accounts
        FOREIGN KEY (linked_account_iban) REFERENCES accounts(iban)
        ON DELETE CASCADE
);

CREATE TABLE personal_loans (
    -- loan_id VARCHAR(50) PRIMARY KEY,
    loan_id BIGINT PRIMARY KEY,
    declared_monthly_income DECIMAL(15, 2) NOT NULL,
    max_allowed_debt_ratio DECIMAL(10, 4) NOT NULL,
    CONSTRAINT fk_personal_loans_loans
        FOREIGN KEY (loan_id) REFERENCES loans(id)
        ON DELETE CASCADE
);

CREATE TABLE business_loans (
    -- loan_id VARCHAR(50) PRIMARY KEY,
    loan_id BIGINT PRIMARY KEY,
    declared_monthly_revenue DECIMAL(15, 2) NOT NULL,
    declared_monthly_expenses DECIMAL(15, 2) NOT NULL,
    max_allowed_debt_ratio DECIMAL(10, 4) NOT NULL,
    CONSTRAINT fk_business_loans_loans
        FOREIGN KEY (loan_id) REFERENCES loans(id)
        ON DELETE CASCADE
);

CREATE TABLE installments (
    id VARCHAR(50) PRIMARY KEY,
    -- loan_id VARCHAR(50) NOT NULL,
    loan_id BIGINT NOT NULL,
    due_date DATE NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    paid BOOLEAN NOT NULL DEFAULT FALSE,
    paid_date DATE,
    penalty_applied BOOLEAN NOT NULL DEFAULT FALSE,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT chk_installments_status
        CHECK (status IN ('PENDING', 'PAID', 'OVERDUE')),
    CONSTRAINT fk_installments_loans
        FOREIGN KEY (loan_id) REFERENCES loans(id)
        ON DELETE CASCADE
);

CREATE TABLE merchants (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    category VARCHAR(30) NOT NULL,
    -- business_client_id VARCHAR(50) NOT NULL,
    business_client_id BIGINT NOT NULL,
    settlement_account_iban VARCHAR(100) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT chk_merchants_category
        CHECK (category IN (
            'STREAMING',
            'TELECOM',
            'UTILITIES',
            'INSURANCE',
            'SHOPPING',
            'OTHER'
        )),
    CONSTRAINT fk_merchants_business_clients
        FOREIGN KEY (business_client_id) REFERENCES business_clients(client_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_merchants_accounts
        FOREIGN KEY (settlement_account_iban) REFERENCES accounts(iban)
        ON DELETE CASCADE
);

CREATE TABLE recurring_payments (
    id VARCHAR(50) PRIMARY KEY,
    -- client_id VARCHAR(50) NOT NULL,
    client_id BIGINT NOT NULL,
    source_account_iban VARCHAR(100) NOT NULL,
    destination_account_iban VARCHAR(100) NOT NULL,
    service_category VARCHAR(30) NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    frequency VARCHAR(20) NOT NULL,
    start_date DATE NOT NULL,
    next_payment_date DATE NOT NULL,
    end_date DATE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    description VARCHAR(255),
    CONSTRAINT chk_recurring_payments_service_category
        CHECK (service_category IN (
            'STREAMING',
            'TELECOM',
            'UTILITIES',
            'INSURANCE',
            'SHOPPING',
            'OTHER'
        )),
    CONSTRAINT chk_recurring_payments_currency
        CHECK (currency IN ('RON', 'EUR')),
    CONSTRAINT chk_recurring_payments_frequency
        CHECK (frequency IN ('WEEKLY', 'MONTHLY', 'YEARLY')),
    CONSTRAINT fk_recurring_payments_clients
        FOREIGN KEY (client_id) REFERENCES clients(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_recurring_payments_source_account
        FOREIGN KEY (source_account_iban) REFERENCES accounts(iban)
        ON DELETE CASCADE,
    CONSTRAINT fk_recurring_payments_destination_account
        FOREIGN KEY (destination_account_iban) REFERENCES accounts(iban)
        ON DELETE CASCADE
);
