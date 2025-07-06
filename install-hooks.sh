#!/bin/bash
#
# Script to install git hooks for the project
#

echo "Installing git hooks..."

# Make sure the hooks directory exists
mkdir -p .git/hooks

# Copy pre-commit hook
cp .githooks/pre-commit .git/hooks/pre-commit

# Make the hook executable
chmod +x .git/hooks/pre-commit

echo "Git hooks installed successfully!"
echo "The pre-commit hook will now automatically format your Java code using Spotless."
