#!/usr/bin/env python3
from collections import Counter


def build_decoder(patterns: list[tuple[int]]) -> dict[str, str]:
    one = next(d for d in patterns if len(d) == 2)
    four = next(d for d in patterns if len(d) == 4)
    return dict((seg,
                 'e' if cnt == 4 else
                 'b' if cnt == 6 else
                 ('d' if seg in four else 'g') if cnt == 7 else
                 ('c' if seg in one else 'a') if cnt == 8 else
                 'f' if cnt == 9 else None)
                for (seg, cnt) in Counter(c for digit in patterns for c in digit).items())


def decode(decoder: dict[str, str], code: tuple[str]) -> int:
    decoded = (''.join(sorted(decoder[segment] for segment in digit)) for digit in code)
    return int(''.join(str(digits.index(digit)) for digit in decoded))


digits = ["abcefg", "cf", "acdeg", "acdfg", "bcdf", "abdfg", "abdefg", "acf", "abcdefg", "abcdfg"]
with open('../../../aoc-21-inputs/year-2021/day-08/example.txt') as f:
    inputs = [[part.split(' ') for part in line.strip().split(" | ")] for line in f.readlines()]
    print(sum(1 for (_, code) in inputs for digit in code if len(digit) in (2, 3, 4, 7)))
    print(sum(decode(build_decoder(patterns), code) for (patterns, code) in inputs))
