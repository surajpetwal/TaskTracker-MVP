#!/bin/bash

# Script to fetch GitHub Actions build logs
# Usage: ./fetch_build_logs.sh [GITHUB_TOKEN]

REPO="surajpetwal/TaskTracker-MVP"
BRANCH="day-10-ui-foundation"
TOKEN="$1"

if [ -z "$TOKEN" ]; then
    echo "Usage: ./fetch_build_logs.sh <GITHUB_TOKEN>"
    echo ""
    echo "To create a GitHub token:"
    echo "1. Go to https://github.com/settings/tokens"
    echo "2. Click 'Generate new token (classic)'"
    echo "3. Select 'repo' and 'workflow' scopes"
    echo "4. Copy the token and run: ./fetch_build_logs.sh YOUR_TOKEN"
    exit 1
fi

echo "Fetching latest build status for branch: $BRANCH..."

# Get the latest workflow run
RUNS_RESPONSE=$(curl -s -H "Authorization: token $TOKEN" \
    "https://api.github.com/repos/$REPO/actions/runs?branch=$BRANCH&per_page=1")

# Extract run ID and status
RUN_ID=$(echo "$RUNS_RESPONSE" | grep -o '"id": [0-9]*' | head -1 | grep -o '[0-9]*')
STATUS=$(echo "$RUNS_RESPONSE" | grep -o '"conclusion": "[^"]*"' | head -1 | cut -d'"' -f4)

if [ -z "$RUN_ID" ]; then
    echo "No workflow runs found for branch $BRANCH"
    exit 1
fi

echo "Latest run ID: $RUN_ID"
echo "Status: $STATUS"
echo ""

# Fetch the logs
if [ "$STATUS" = "failure" ] || [ "$STATUS" = "success" ]; then
    echo "Downloading logs..."
    
    # Get logs URL
    LOGS_URL=$(curl -s -H "Authorization: token $TOKEN" \
        -H "Accept: application/vnd.github.v3+json" \
        "https://api.github.com/repos/$REPO/actions/runs/$RUN_ID/logs" \
        -D - | grep -i "location:" | cut -d' ' -f2 | tr -d '\r')
    
    if [ -n "$LOGS_URL" ]; then
        # Download and extract logs
        curl -s -L -H "Authorization: token $TOKEN" "$LOGS_URL" -o build_logs.zip
        unzip -q build_logs.zip -d build_logs/ 2>/dev/null
        
        # Find and display the error
        echo ""
        echo "=== BUILD ERRORS ==="
        grep -r "error:" build_logs/ 2>/dev/null | head -20
        
        echo ""
        echo "=== FULL LOGS AVAILABLE IN: build_logs/ ==="
        ls -la build_logs/
    else
        echo "Could not get logs URL. Trying alternative method..."
        
        # Get job details
        curl -s -H "Authorization: token $TOKEN" \
            "https://api.github.com/repos/$REPO/actions/runs/$RUN_ID/jobs" \
            > jobs.json
        
        echo "Job details saved to jobs.json"
        cat jobs.json | grep -o '"name": "[^"]*"' | head -10
    fi
else
    echo "Build status: $STATUS (not completed yet)"
fi

# Save status for reference
echo "$STATUS" > .last_build_status
echo "Run ID: $RUN_ID" >> .last_build_status
echo "Status saved to .last_build_status"
