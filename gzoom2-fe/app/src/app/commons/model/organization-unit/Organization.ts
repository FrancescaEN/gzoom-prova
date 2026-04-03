import { VarUtils } from '../../utils/var-utils';

export class Organization {
  id?: string;
  name: string;
  nameLang?: string;
  usr?: string;

  static fromJson(src: any): Organization {
    const tgt = new Organization();
    if (!VarUtils.isDefined(src)) return tgt;

    tgt.id = src.id;
    tgt.name = src.name;
    tgt.nameLang = src.nameLang;
    tgt.usr = src.usr;

    return tgt;
  }
}
