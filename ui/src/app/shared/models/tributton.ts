export enum TriState {
  LEFT = 1,
  MIDDLE = 2,
  RIGHT = 3
}

export class TriEvent {
  constructor(readonly state: TriState) {}

  get mat(): [boolean, boolean] {
    switch(this.state) {
      case TriState.LEFT: return [true, false];
      case TriState.MIDDLE: return [true,true];
      case TriState.RIGHT: return [false, true];
      default: return [false, false];
    }
  }
}
