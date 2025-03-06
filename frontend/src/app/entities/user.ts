export class User
{
    public name?: string;
    public surname?: string;
    public email?: string;
    public account?: string;
    public password?: string;
    public role?: number;

    constructor(json?: any)
    {
        if (json)
        {
            this.name = json.name;
            this.surname = json.surname;
            this.email = json.email;
            this.account = json.account;
            this.password = json.password;
            this.role = json.role;
        }
    }
}
