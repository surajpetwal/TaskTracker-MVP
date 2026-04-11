#!/bin/bash

# Simple script to check build status using curl (no authentication required for public repos)
# For detailed logs, you need a GitHub token

REPO="surajpetwal/TaskTracker-MVP"
BRANCH="day-3-crud"

echo "Checking build status for $REPO (branch: $BRANCH)..."
echo ""

# Fetch latest workflow run (public repo - no auth needed)
curl -s "https://api.github.com/repos/$REPO/actions/runs?branch=$BRANCH&per_page=5" > workflow_runs.json

# Parse and display
if [ -f workflow_runs.json ]; then
    echo "Recent builds:"
    echo "=============="
    
    # Extract run info
    grep -o '"conclusion": "[^"]*"\|"status": "[^"]*"\|"name": "[^"]*"\|"html_url": "[^"]*"' workflow_runs.json | head -20 | while read line; do
        echo "$line" | sed 's/"//g; s/,//g'
    done
    
    echo ""
    echo "Full details saved to: workflow_runs.json"
    
    # Check for failures
    if grep -q '"conclusion": "failure"' workflow_runs.json; then
        echo ""
        echo "❌ FAILED BUILDS DETECTED"
        echo "View details at: https://github.com/$REPO/actions?query=branch%3A$BRANCH"
    elif grep -q '"conclusion": "success"' workflow_runs.json; then
        echo ""
        echo "✅ RECENT BUILDS PASSED"
    fi
else
    echo "Failed to fetch workflow runs"
fi
