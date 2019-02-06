# Build Summary panel using Atlassian SDK

- `atlas-run`
- Quick Reload - `atlas-mvn package` (in other terminal)
- in SummaryPanel.java add /bamboo to `String summaryLink = "/bamboo/artifact";`

## Pre-requisities

- plan with key `CT-TEST`
- result job with ID `RES`
- artifact `Logs` where are located files including `0summary.html` file
