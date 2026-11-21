#!/usr/bin/env bash
# Creates a GitHub Project (Kanban board), links it to the repo and adds every issue
# listed in scripts/created-issues.txt (produced by create-github-issues.sh).
#
# Prerequisites:
#   gh auth refresh -s project      (adds the "project" scope to your gh login)
# Usage: bash scripts/setup-project-board.sh
set -euo pipefail
cd "$(dirname "$0")/.."

REPO=$(gh repo view --json nameWithOwner --jq .nameWithOwner)
OWNER=${REPO%%/*}
TITLE="SauceDemo QA - Bug Board"

NUMBER=$(gh project create --owner "$OWNER" --title "$TITLE" --format json --jq .number)
echo "Created project #$NUMBER"
gh project link "$NUMBER" --owner "$OWNER" --repo "$REPO"

while read -r url; do
  [ -z "$url" ] && continue
  gh project item-add "$NUMBER" --owner "$OWNER" --url "$url"
  echo "Added $url"
done < scripts/created-issues.txt

PROJECT_URL=$(gh project view "$NUMBER" --owner "$OWNER" --format json --jq .url)
echo
echo "Board: $PROJECT_URL"
echo "Next (one-time, 30 seconds in the browser):"
echo "  1. Open the board, switch the view to 'Board' layout (Kanban)."
echo "  2. GitHub's default Status columns are Todo / In Progress / Done - rename 'Todo' to 'To Do' via the Status field settings."
echo "  3. Drag at least one issue into 'In Progress' / 'Done' to show the workflow."
