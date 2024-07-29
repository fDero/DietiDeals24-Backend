
CREATE FUNCTION id_by_username(input_username VARCHAR(30), OUT res INT) RETURNS INT AS $$
BEGIN   
    SELECT Account.account_id INTO res FROM Account WHERE Account.username = input_username; 
    RETURN;
END
$$ LANGUAGE plpgsql;