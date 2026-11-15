#!/bin/sh
cd "$(dirname "$0")/.." || exit 1
[ -d app/src/main ] || { echo "app/src/main not found; run from the project root"; exit 2; }
grep -rnE --include=*.kt 'offset\((x|y) *= *-|absoluteOffset|clipToBounds\(\)|requiredHeight\(|translation(X|Y) *=|\.layout *\{|padding\([^)]*= *-[0-9]' app/src/main || echo "no suspicious layout patterns found"
