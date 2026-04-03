import { ResultMessage } from './result-message';

export class Results<T> {
  data?: T;
  messages?: ResultMessage[];
}
