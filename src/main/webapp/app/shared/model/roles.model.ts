export interface IRoles {
  id?: number;
  roleName?: string;
}

export class Roles implements IRoles {
  constructor(public id?: number, public roleName?: string) {}
}
