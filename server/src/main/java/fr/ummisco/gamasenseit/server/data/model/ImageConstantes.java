package fr.ummisco.gamasenseit.server.data.model;

import java.util.Base64;

public class ImageConstantes {
    public static final byte[]
            DEFAULT_IMG = Base64.getDecoder().decode("R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw=="),
            NO_IMG = Base64.getDecoder().decode("iVBORw0KGgoAAAANSUhEUgAAAEAAAABABAMAAABYR2ztAAAABGdBTUEAALGPC/xhBQAAAAFzUkdCAK7OHOkAAAAwUExURUdwTAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACVM9DkAAAAPdFJOUwASwwEh3NrBOaJPgnTs0+TcAvgAAAGRSURBVEjH3ZU9TsNAEEYdLQVKhZ3CIKDYhtag0LiGggYhIVG7o/cJLOhRhHIAOAESJzASB8gBKOhpIlkkhEAYdnaNw473p4apUjx9yj5/uxME/2W27j3AqFpzAyVwNxDBmzuik0DujohFBOuT0SIKyNMzMi96RBUmoM8XieAxAabkIFV4qM1AB/YS6uJEA9h1ywUBTkPqggAZj5uI/QMT8Iou5O/VZHprAtCFjNgGmBkBdCE/PsC7EUAXGHGjJBqAjnJRWhPqXhwDzC2A6kU3WzxYgEC5WL8ILAALlYu+DUj5jwsLMKrCovmoBqALwKMmwgCI82HxRAQ76plUj0FGiINcwtNuG0ixiBiRrzwDDFsAK2VVsRdY8HkL6Kou40HyCGAyoMBV3XZe35GCALMMlhHir5J7wbJJc2HQBUYQYHmjpIvIAUgXArID0kXkAGoXDkC5cADKxY4DkC42HIB00XMBdS/sQN2LXzOGdoT2XtydkxnG3ue38D+/lTfCuwQ+As8S+PRsos1H3zJjf2XrfgOc7tH0up79SwAAAABJRU5ErkJggg==");
}
