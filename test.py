import requests
import contextlib

URL = "https://localhost:8443/"
with requests.Session() as s, contextlib.redirect_stderr(None):
    auth = s.post(
        url=URL + "auth/login",
        json={"username": " ", "password": " "},
        verify=False
    )
    s.headers.setdefault("Authorization", "Bearer " + auth.json()["token"])
    res = s.get(URL + "private/access", verify=False)
    print(res.text)
