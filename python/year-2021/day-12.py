#!/usr/bin/env python3

def traverse(edges: dict[str, set[str]], path: list[str] = None, allow_twice: bool = False):
    if path is None:
        path = ["start"]
    for option in edges[path[-1]]:
        new_path = path + [option]
        if option == 'end':
            yield new_path
        elif option[0].islower() and option in path:
            if allow_twice:
                yield from traverse(edges, new_path, False)
        else:
            yield from traverse(edges, new_path, allow_twice)


def parse(pairs):
    result = {}
    for (a, b) in pairs:
        if b != "start":
            result.setdefault(a, []).append(b)
        if a != "start":
            result.setdefault(b, []).append(a)
    return result


def main():
    with open('../../../aoc-21-inputs/year-2021/day-12/input.txt') as f:
        edges = parse(x.strip().split("-") for x in f.readlines())
    print("part1", sum(1 for _ in traverse(edges)))
    print("part2", sum(1 for _ in traverse(edges, allow_twice=True)))


if __name__ == '__main__':
    main()
