export const
  DELAY_NO_SIGNAL = 3600000,
  DELAY_DEAD = 86400000,
  DEFAULT_LNG = 0,
  DEFAULT_LAT = 30,
  DEFAULT_CENTER = {lat: DEFAULT_LAT, lng: DEFAULT_LNG},
  LEAFLET_ATTRIBUTION = "&copy; <a href='http://www.openstreetmap.org/copyright'>OpenStreetMap</a>",
  LEAFLET_ATTRIBUTION_SMALL = "&copy; <a href='http://www.openstreetmap.org/copyright'>OSM</a>",
  LEAFLET_URL = "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", // "https://localhost:8443/{s}-{z}-{x}-{z}.png",
  DEFAULT_IMG = 'data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==',
  NO_IMG = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABABAMAAABYR2ztAAAABGdBTUEAALGPC/xhBQAAAAFzUkdCAK7OHOkAAAAwUExURUdwTAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACVM9DkAAAAPdFJOUwASwwEh3NrBOaJPgnTs0+TcAvgAAAGRSURBVEjH3ZU9TsNAEEYdLQVKhZ3CIKDYhtag0LiGggYhIVG7o/cJLOhRhHIAOAESJzASB8gBKOhpIlkkhEAYdnaNw473p4apUjx9yj5/uxME/2W27j3AqFpzAyVwNxDBmzuik0DujohFBOuT0SIKyNMzMi96RBUmoM8XieAxAabkIFV4qM1AB/YS6uJEA9h1ywUBTkPqggAZj5uI/QMT8Iou5O/VZHprAtCFjNgGmBkBdCE/PsC7EUAXGHGjJBqAjnJRWhPqXhwDzC2A6kU3WzxYgEC5WL8ILAALlYu+DUj5jwsLMKrCovmoBqALwKMmwgCI82HxRAQ76plUj0FGiINcwtNuG0ixiBiRrzwDDFsAK2VVsRdY8HkL6Kou40HyCGAyoMBV3XZe35GCALMMlhHir5J7wbJJc2HQBUYQYHmjpIvIAUgXArID0kXkAGoXDkC5cADKxY4DkC42HIB00XMBdS/sQN2LXzOGdoT2XtydkxnG3ue38D+/lTfCuwQ+As8S+PRsos1H3zJjf2XrfgOc7tH0up79SwAAAABJRU5ErkJggg==",
  SM = 576,
  MD = 768,
  LG = 992,
  XL = 1200,
  XXL = 1400;

export enum AccessUserPrivilege {
  MANAGE = 0, // peut ajouter des capteurs et utilisateur mais pas les modifiers
  VIEW = 1  // peut lire les capteurs
}

export enum AccessPrivilege {
  MAINTENANCE = 0, // modifié le capteur (propriétaire)
  SOCIAL = 1 // lire les données
}


