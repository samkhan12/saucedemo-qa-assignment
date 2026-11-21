#!/usr/bin/env bash
# Creates one GitHub Issue per bug (docs/bugs/BUG-00X.md) with screenshots embedded inline.
#
# Prerequisites:
#   1. gh CLI installed and logged in:  gh auth login
#   2. The repo already exists on GitHub and this folder is pushed to it INCLUDING docs/screenshots/*.png
#      (run `npm run evidence` first, then commit + push the screenshots, then run this script).
# Usage: bash scripts/create-github-issues.sh
set -euo pipefail
cd "$(dirname "$0")/.."

REPO=$(gh repo view --json nameWithOwner --jq .nameWithOwner)
BRANCH=$(gh repo view --json defaultBranchRef --jq .defaultBranchRef.name)
RAW="https://github.com/${REPO}/blob/${BRANCH}/docs/screenshots"   # ?raw=true makes GitHub render it inline
OUT=scripts/created-issues.txt
: > "$OUT"

# Labels (ignore errors if they already exist)
gh label create bug               --color d73a4a --description "Something isn't working"        2>/dev/null || true
gh label create "severity: critical" --color b60205 2>/dev/null || true
gh label create "severity: high"     --color d93f0b 2>/dev/null || true
gh label create "severity: medium"   --color fbca04 2>/dev/null || true
gh label create "priority: P1"       --color 5319e7 2>/dev/null || true
gh label create "priority: P2"       --color 1d76db 2>/dev/null || true

# id | title | severity label | priority label
BUGS=(
  "BUG-001|[problem_user] All products show the same image on the inventory page|severity: medium|priority: P2"
  "BUG-002|[problem_user] Product sorting does not reorder the list|severity: high|priority: P2"
  "BUG-003|[problem_user] Add to cart / Remove buttons do not work for several products|severity: high|priority: P1"
  "BUG-004|[problem_user] Checkout: typing Last Name overwrites First Name - order cannot be placed|severity: critical|priority: P1"
  "BUG-005|[performance_glitch_user] Login takes several seconds with no loading indicator|severity: medium|priority: P2"
  "BUG-006|[standard_user] Checkout and order completion are allowed with an empty cart|severity: medium|priority: P2"
)

for entry in "${BUGS[@]}"; do
  IFS='|' read -r id title sev pri <<< "$entry"
  body=$(mktemp)
  cat "docs/bugs/${id}.md" > "$body"
  printf '\n\n## Screenshots\n' >> "$body"
  found=0
  for img in docs/screenshots/${id}-*.png; do
    [ -e "$img" ] || continue
    found=1
    name=$(basename "$img")
    printf '\n![%s](%s/%s?raw=true)\n' "$name" "$RAW" "$name" >> "$body"
  done
  if [ "$found" -eq 0 ]; then
    echo "WARNING: no screenshots found for $id - run 'npm run evidence' and push docs/screenshots first" >&2
  fi
  url=$(gh issue create --title "[$id] $title" --body-file "$body" --label bug --label "$sev" --label "$pri")
  echo "$url" | tee -a "$OUT"
  rm -f "$body"
done

echo "Done. Issue URLs saved to $OUT"
