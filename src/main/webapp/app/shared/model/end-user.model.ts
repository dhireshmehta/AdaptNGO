export interface IEndUser {
  id?: number;
  email?: string;
  firstName?: string;
  lastName?: string;
  roles?: string;
}

export class EndUser implements IEndUser {
  constructor(public id?: number, public email?: string, public firstName?: string, public lastName?: string, public roles?: string) {}
}
