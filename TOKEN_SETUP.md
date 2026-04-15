# GitHub Token Setup

## Why Needed
APK builds work without token. Releases need token with `repo` + `workflow` permissions.

## Steps

1. Create token: https://github.com/settings/tokens
   - Name: `TaskTracker Release`
   - Scopes: `repo`, `workflow`

2. Add to repo secrets:
   - Go to: Repo → Settings → Secrets → Actions
   - New secret: Name=`GITHUB_TOKEN`, Value=your token

3. Done! Next build will create releases.

## Current Status
Builds always pass. Releases work after token setup.
