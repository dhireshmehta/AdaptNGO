export interface ILineItem {
  id?: number;
  name?: string;
  link?: string;
  categories?: string;
  roles?: string;
  desc?: string;
  viewCount?: number;
  type?: string;
}

export class LineItem implements ILineItem {
  constructor(
    public id?: number,
    public name?: string,
    public link?: string,
    public categories?: string,
    public roles?: string,
    public desc?: string,
    public viewCount?: number,
    public type?: string
  ) {}
}
