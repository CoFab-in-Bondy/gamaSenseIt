#!/usr/bin/env python

"""
Generator of random data to test gamasensit.
"""

__author__ = "***REMOVED*** ***REMOVED***"
__copyright__ = "Copyright (C) 2017-2021  UMMISCO - IRD"
__license__ = "GPL"
__version__ = "2.0.2"

from random import choice, choices, randint, random, randbytes
from typing import Union
from string import punctuation, ascii_letters
from datetime import datetime, timedelta
from math import floor, ceil

import argparse
import lorem


SEP = punctuation.replace("'", "").replace("\\", "")
START_ID = 1042


def dbid() -> int:
    """Generate a unique id in the script."""
    # pylint: disable=global-statement
    global START_ID
    tmp = START_ID
    START_ID += 1
    return tmp


def randblob(data_format: int) -> bytes:
    """Generate random data with a specified format."""
    if data_format == 0:
        return randbytes(4)
    if data_format == 1:
        return randbytes(8)
    if data_format == 2:
        return bytes(choice(ascii_letters) + lorem.sentence(), "utf8")
    raise TypeError("Invalid data_format")


def randdatetime(delta: int = 3000000) -> datetime:
    """Return a random datetime with a given delta."""
    return datetime.fromtimestamp(
        datetime.now().timestamp() - delta + randint(0, delta)
    )


def randrange(bot: float, top: float, w: float = 1.0) -> range:
    """Create a range start at 0 and end between bot * w and top * w."""
    return range(randint(floor(bot * w), ceil(top * w)))


def randlat(precision: int = 3) -> float:
    """Generate random latitude between -90 and 90"""
    return round(random() * 180 - 90, precision)


def randlong(precision: int = 3) -> float:
    """Generate random longitude between -180 and 180"""
    return round(random() * 360 - 180, precision)


def insert_unsafe(
    table: str,
    record: tuple[Union[int, str, float, bytearray, bytes, datetime], ...]
) -> str:
    """Insert unsafe tuple in table"""
    inside = []
    for v in record:
        if isinstance(v, (int, float)):
            inside.append(str(v))
        elif isinstance(v, str):
            inside.append(f"'{v}'")
        elif isinstance(v, (bytes, bytearray)):
            inside.append(f"x'{v.hex().upper()}'")
        elif isinstance(v, datetime):
            inside.append(f"'{v:%Y-%m-%d %H:%M:%S}'")
        elif v is None:
            inside.append("NULL")
    return f"INSERT INTO {table} VALUES ({', '.join(inside)});\n"


def generate_data(w: float = 1.0) -> str:
    """Generate random sql script of data."""
    stm = ""
    for sm in randrange(3, 5, w):
        sm_id = dbid()
        stm += insert_unsafe("sensor_metadata", (
            sm_id,
            choice(SEP),
            lorem.sentence(),
            f"meta-sensor-{sm}",
            f"v{randint(0, ceil(3 * w))}.{randint(0, 9)}.{randint(0, 9)}"
        ))

        pms = []
        for pm in randrange(3, 8):
            pm_id = dbid()
            pm_record = (
                pm_id,
                randint(0, 2),  # data_format : (int, float, string)
                randint(0, 6),  # data_parameter : (0-6)
                f"icon-{randint(1, ceil(20 * w))}",
                pm,
                f"meta-parameter-sm={sm}-{pm}",
                sm_id,
                ''.join(choices(ascii_letters, k=2)),
            )
            pms.append(pm_record)
            stm += insert_unsafe("parameter_metadata", pm_record)

        for s in randrange(8, 12, w):
            s_id = dbid()
            stm += insert_unsafe("sensor", (
                s_id,
                f"sensor-sm={sm}-{s}",
                "Oops, " + lorem.sentence(),
                1 if random() * 10 < 1 else 0,
                randlat(),
                randlong(),
                f"sensor-name-{sm}.{s}",
                sm_id,
                lorem.sentence(),
                None
            ))

            # 0, 1, 8, 27 hours
            deadtime = timedelta(hours=randint(0, 3) ** 3)
            for _ in randrange(50, 2000, w):
                capture_date = randdatetime() - deadtime
                for pm_record in pms:
                    p_id = dbid()
                    stm += insert_unsafe("parameter", (
                        p_id,
                        capture_date,
                        randblob(pm_record[1]),  # data
                        pm_record[0],
                        s_id
                    ))
    return stm


if __name__ == "__main__":
    parser = argparse.ArgumentParser(
        "randdata",
        description="Generator of random data to test gamasensit"
    )
    parser.add_argument(
        "-o",
        "--out",
        help="Output file",
        default="import.sql"
    )
    parser.add_argument(
        "-w",
        "--weigth",
        default=1.0,
        help="Abstract indicator of the number of data generated",
        type=float
    )
    args = parser.parse_args()
    print("[+] Generating ...")
    data = generate_data(args.weigth)
    line = data.count('\n')
    print(f"[+] Generated {line} INSERT")
    with open(args.out, "w", encoding="utf8") as file:
        file.write(data)
    line = data.count('\n')
    print("[+] Done !")
