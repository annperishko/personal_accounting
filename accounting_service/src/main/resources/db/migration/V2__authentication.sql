ALTER TABLE IF EXISTS users
ADD COLUMN IF NOT EXISTS name text NOT NULL,
ADD COLUMN IF NOT EXISTS surname text NOT NULL ,
ADD COLUMN IF NOT EXISTS password VARCHAR(60) NOT NULL,
ADD COLUMN IF NOT EXISTS auth_provider VARCHAR(60) NOT NULL DEFAULT 'default';
