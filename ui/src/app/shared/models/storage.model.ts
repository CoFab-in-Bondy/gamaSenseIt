export class StorageModel<T> {

  private static _storages: StorageModel<any>[] = [];

  constructor(private key: string, private defaultValue: T) {
    StorageModel._storages.push(this);
  }

  public static clearAllStorages() {
    StorageModel._storages.forEach(storage=>storage.del());
  }

  get(): T {
    let latlng = this.defaultValue;
    const latlngStr = sessionStorage.getItem(this.key);
    if (latlngStr)
      latlng = <T>JSON.parse(latlngStr);
    return latlng;
  }

  set(value: T): void {
    sessionStorage.setItem(this.key, JSON.stringify(value))
  }

  del(): void {
    sessionStorage.removeItem(this.key);
  }
}
