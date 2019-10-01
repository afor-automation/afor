google.charts.load('current', {packages: ['corechart', 'table']});
var scenarioBreakdownData;
var scenarioDurationData;

google.charts.setOnLoadCallback(drawScenarioDataCharts);
google.charts.setOnLoadCallback(drawFeatureScenarioBreakdownCharts);
google.charts.setOnLoadCallback(drawScenarioDurationCharts);

$(window).on("throttledresize", function (event) {
    drawCharts();
});

function drawCharts() {
    drawScenarioDataCharts();
    drawFeatureScenarioBreakdownCharts();
    drawScenarioDurationCharts();
}

function drawScenarioDataCharts() {
	writeBreakdown('scenarioSummaryBreakdown');
}

function drawFeatureScenarioBreakdownCharts() {
    if (null == scenarioBreakdownData) {
        scenarioBreakdownData = getFeatureScenarioBreakdownData();
    }
	drawStackedBarChart(scenarioBreakdownData, getTotalFeatureCount(), 'featureScenarioBarChart', 'Feature Results (' + getTotalFeatureCount() + ' in total)');
}

function drawScenarioDurationCharts() {
    if (null == scenarioDurationData) {
        scenarioDurationData = getScenarioDurationData();
    }
    drawLineChart(scenarioDurationData, 'scenarioPerformanceLineChart', 'Scenario duration (Total duration ' + getTotalDurationText() + ')', 'Duration in seconds');
}

function getTotalFeatureCount() {
    return formatterHighLevelSummary.features.passed + formatterHighLevelSummary.features.failed + formatterHighLevelSummary.features.undefined + formatterHighLevelSummary.features.pending + formatterHighLevelSummary.features.skipped + formatterHighLevelSummary.features.ambiguous;
}

function getScenarioCount() {
    return formatterHighLevelSummary.scenarios.passed + formatterHighLevelSummary.scenarios.failed + formatterHighLevelSummary.scenarios.undefined + formatterHighLevelSummary.scenarios.pending + formatterHighLevelSummary.scenarios.skipped + formatterHighLevelSummary.scenarios.ambiguous;
}

function getStepCount() {
    return formatterHighLevelSummary.steps.passed + formatterHighLevelSummary.steps.failed + formatterHighLevelSummary.steps.undefined + formatterHighLevelSummary.steps.pending + formatterHighLevelSummary.steps.skipped + formatterHighLevelSummary.steps.ambiguous;
}

function getTotalDurationText() {
	return formatterSummaryDuration;
}

function getScenarioData() {
	    var data = new google.visualization.DataTable();
	    data.addColumn('string', 'Scenario');
	    data.addColumn('number', 'Scenarios');

	    data.addRows([
	    ['Passed', formatterHighLevelSummary.scenarios.passed],
	    ['Failed', formatterHighLevelSummary.scenarios.failed],
	    ['Undefined', formatterHighLevelSummary.scenarios.undefined],
	    ['Pending', formatterHighLevelSummary.scenarios.pending],
	    ['Skipped', formatterHighLevelSummary.scenarios.skipped],
	    ['Ambiguous', formatterHighLevelSummary.scenarios.ambiguous]
    ]);
    return data;
}

function getFeatureScenarioBreakdownData() {
	var data = new google.visualization.DataTable();
	data.addColumn('string', 'Feature');
	data.addColumn('number', 'Passed');
	data.addColumn('number', 'Failed');
	if (formatterHighLevelSummary.scenarios.undefined > 0 || formatterHighLevelSummary.scenarios.pending > 0 || formatterHighLevelSummary.scenarios.ambiguous > 0) {
		data.addColumn('number', 'Undefined');
    	data.addColumn('number', 'Pending');
    	data.addColumn('number', 'Ambiguous');
	}

	for(var feature = 0; feature < formatterSummary.length; feature++) {
		var resultPassed = 0;
		var resultFailed = 0;
		var resultUndefined = 0;
		var resultPending = 0;
		var resultSkipped = 0;
		var resultAmbiguous = 0;
		for(var scenario = 0; scenario < formatterSummary[feature].scenarios.length; scenario++) {
            var scenarioResult = null;
			for(var step = 0; step < formatterSummary[feature].scenarios[scenario].steps.length; step++) {
				if (formatterSummary[feature].scenarios[scenario].steps[step].result != null) {
					switch(formatterSummary[feature].scenarios[scenario].steps[step].result) {
						case "PASSED":
							if (scenarioResult == null) {
								scenarioResult = "passed";
							}
							break;
						case "FAILED":
							scenarioResult = "failed";
							break;
						case "UNDEFINED":
							if (scenarioResult == null || scenarioResult == "passed") {
								scenarioResult = "undefined";
							}
							break;
						case "PENDING":
							if (scenarioResult == null || scenarioResult == "passed") {
								scenarioResult = "pending";
							}
							break;
                        case "SKIPPED":
                          if (scenarioResult == null || scenarioResult == "passed") {
                            scenarioResult = "skipped";
                          }
                          break;
                        case "AMBIGUOUS":
                          if (scenarioResult == null || scenarioResult == "passed") {
                            scenarioResult = "ambiguous";
                          }
                          break;
					}
				}
			}
			if (null != scenarioResult) {
                switch(scenarioResult) {
                    case "passed":
                        resultPassed++;
                        break;
                    case "failed":
                        resultFailed++;
                        break;
                    case "undefined":
                        resultUndefined++;
                        break;
                    case "pending":
                        resultPending++;
                    case "skipped":
                        resultSkipped++;
                    case "ambiguous":
                        resultAmbiguous++;
                        break;
                }
            }
		}
        for(var scenario = 0; scenario < formatterSummary[feature].scenarioOutlines.length; scenario++) {
            var scenarioResult = null;
            for(var step = 0; step < formatterSummary[feature].scenarioOutlines[scenario].steps.length; step++) {
                if (formatterSummary[feature].scenarioOutlines[scenario].steps[step].result != null) {
                    switch(formatterSummary[feature].scenarioOutlines[scenario].steps[step].result) {
                        case "PASSED":
                            if (scenarioResult == null) {
                                scenarioResult = "passed";
                            }
                            break;
                        case "FAILED":
                            scenarioResult = "failed";
                            break;
                        case "UNDEFINED":
                            if (scenarioResult == null || scenarioResult == "passed") {
                                scenarioResult = "undefined";
                            }
                            break;
                        case "PENDING":
                            if (scenarioResult == null || scenarioResult == "passed") {
                                scenarioResult = "pending";
                            }
                            break;
                        case "SKIPPED":
                          if (scenarioResult == null || scenarioResult == "passed") {
                            scenarioResult = "skipped";
                          }
                          break;
                        case "AMBIGUOUS":
                          if (scenarioResult == null || scenarioResult == "passed") {
                            scenarioResult = "ambiguous";
                          }
                          break;
                    }
                }
            }
            if (null != scenarioResult) {
                switch(scenarioResult) {
                    case "passed":
                        resultPassed++;
                        break;
                    case "failed":
                        resultFailed++;
                        break;
                    case "undefined":
                        resultUndefined++;
                        break;
                    case "pending":
                        resultPending++;
                    case "skipped":
                        resultSkipped++;
                    case "ambiguous":
                        resultAmbiguous++;
                        break;
                }
            }
        }
        if (formatterHighLevelSummary.scenarios.undefined > 0 || formatterHighLevelSummary.scenarios.pending > 0 || formatterHighLevelSummary.scenarios.ambiguous > 0) {
            data.addRow([formatterSummary[feature].name, resultPassed == 0 ? null : resultPassed, resultFailed == 0 ? null : resultFailed, resultUndefined == 0 ? null : resultUndefined, resultPending == 0 ? null : resultPending, resultAmbiguous == 0 ? null : resultAmbiguous]);
        } else {
            data.addRow([formatterSummary[feature].name, resultPassed == 0 ? null : resultPassed, resultFailed == 0 ? null : resultFailed]);
        }
    }
    return data;
}

function getScenarioDurationData() {
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'Scenario');
    data.addColumn('number', 'Scenario duration');

    for(var feature = 0; feature < formatterSummary.length; feature++) {
        for(var scenario = 0; scenario < formatterSummary[feature].scenarios.length; scenario++) {
			var scenarioDuration = formatterSummary[feature].scenarios[scenario].duration;
       	    data.addRow([formatterSummary[feature].scenarios[scenario].name + " - (" + formatterSummary[feature].scenarios[scenario].steps.length + " steps)", (Math.round((scenarioDuration / 1000000) + 0.00001) * 1000) / 1000000]);
        }
    }
    for(var feature = 0; feature < formatterSummary.length; feature++) {
        for(var scenario = 0; scenario < formatterSummary[feature].scenarioOutlines.length; scenario++) {
			var scenarioDuration = formatterSummary[feature].scenarioOutlines[scenario].duration;
       	    data.addRow([formatterSummary[feature].scenarioOutlines[scenario].name + " - (" + formatterSummary[feature].scenarioOutlines[scenario].steps.length + " steps)", (Math.round((scenarioDuration / 1000000) + 0.00001) * 1000) / 1000000]);
        }
    }
    return data;
}

function drawStackedBarChart(data, total, elementId, title) {
	var options = {
        title: title,
        width: "100%",
        height: "100%",
        axisTitlesPosition: 'inAndOut',
        chartArea: {
            left: "25%",
            top: "25%",
            bottom: "15%",
            right: "2%",
            height: "100%",
            width: "100%"
        },
        colors: ['#b0d633',
            '#ed2525',
            '#e07a3e',
            '#eaec2d',
            '#2deaec',
            '#ffc733']
        ,
        hAxis: {format: '0', viewWindowMode: 'pretty'},
        vAxis: {showTextEvery: 1},
        bars: 'horizontal',
        legend: { position: 'top', maxLines: 3 },
        bar: { groupWidth: '90%' },
        isStacked: true,
        titleTextStyle: {
            fontSize: '18',
            bold: true
        }
    };
    new google.visualization.BarChart(document.getElementById(elementId)).draw(data, options);
}

function drawLineChart(data, elementId, title, vAxisTitle) {
	var options = {
        title: title,
        width: "100%",
        height: "100%",
		vAxis: {
            title: vAxisTitle
	    },
	    hAxis: { textPosition: 'none' },
        legend: { position: 'none' },
        titleTextStyle: {
            fontSize: '18',
            bold: true
        }
    };
    new google.visualization.LineChart(document.getElementById(elementId)).draw(data, options);
}

function writeBreakdown(elementId) {
	var element = document.getElementById(elementId);
	element.innerHTML = "<div class=\"breakdownHeading chartHeading\">Breakdown</div>";
	element.innerHTML += "<div class=\"breakdownRow\">" + getTotalFeatureCount() + " <b>Features</b> (<span class=\"passed\">" + formatterHighLevelSummary.features.passed + " passed,</span>	 <span class=\"failed\">" + formatterHighLevelSummary.features.failed + " failed,</span> <span class=\"undefined\">" + formatterHighLevelSummary.features.undefined + " undefined,</span> <span class=\"pending\">" + formatterHighLevelSummary.features.pending + " pending</span>)</div>";
	element.innerHTML += "<div class=\"breakdownRow\">" + getScenarioCount() + " <b>Scenarios</b> (<span class=\"passed\">" + formatterHighLevelSummary.scenarios.passed + " passed,</span> <span class=\"failed\">" + formatterHighLevelSummary.scenarios.failed + " failed,</span> <span class=\"undefined\">" + formatterHighLevelSummary.scenarios.undefined + " undefined,</span> <span class=\"pending\">" + formatterHighLevelSummary.scenarios.pending + " pending</span>)</div>";
	element.innerHTML += "<div class=\"breakdownRow\">" + getStepCount() + " <b>Steps</b> (<span class=\"passed\">" + formatterHighLevelSummary.steps.passed + " passed,</span> <span class=\"failed\">" + formatterHighLevelSummary.steps.failed + " failed,</span> <span class=\"undefined\">" + formatterHighLevelSummary.steps.undefined + " undefined,</span> <span class=\"pending\">" + formatterHighLevelSummary.steps.pending + " pending,</span> <span class=\"skipped\">" + formatterHighLevelSummary.steps.skipped + " skipped</span>  <span class=\"skipped\">" + formatterHighLevelSummary.steps.ambiguous + " ambiguous</span>)</div>";
}