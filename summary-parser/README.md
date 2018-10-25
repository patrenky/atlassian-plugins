# Build Summary parser using Atlassian SDK

https://developer.atlassian.com/server/bamboo/test-collection-and-reporting/

- `atlas-run`
- Quick Reload - `atlas-mvn package` (in other terminal)

## How build_logs/summary-parse.log looks like

Suite | Test | Duration | State

FirefoxTests|TestThatLoginAppears|25|SUCCESS\
FirefoxTests|TestUserProfileIsShown|11|SUCCESS\
FirefoxTests|CanLogout|15|FAIL\
SafariTests|TestThatLoginAppears|25|SUCCESS\
SafariTests|TestUserProfileIsShown|11|SUCCESS\
SafariTests|CanLogout|15|FAIL
