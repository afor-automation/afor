google.charts.load('current', {packages: ['corechart']});
google.charts.setOnLoadCallback(drawPieCharts);

function drawPieCharts() {
	drawPieChart(getFeatureData(), 'featurePieChart', 'Feature Test Results (' + getTotalFeatureCount() + ' in total)');
	drawPieChart(getScenarioData(), 'scenarioPieChart', 'Scenario Test Results (' + getScenarioCount() + ' in total)');
	drawPieChart(getStepData(), 'stepPieChart', 'Step Test Results (' + getStepCount() + ' in total)');
}

function getTotalFeatureCount() {
	return formatterSummary.length;
}

function getScenarioCount() {
	var total = 0;

	for(var feature = 0; feature < formatterSummary.length; feature++) {
		total += formatterSummary[feature].scenarios.length;
	}
	return total;
}

function getStepCount() {
	var total = 0;

	for(var feature = 0; feature < formatterSummary.length; feature++) {
		for(var scenario = 0; scenario < formatterSummary[feature].scenarios.length; scenario++) {
			for(var step = 0; step < formatterSummary[feature].scenarios[scenario].steps.length; step++) {
				if (formatterSummary[feature].scenarios[scenario].steps[step].result != null) {
					total++;
				}
			}
		}
	}
	return total;
}

function getStepData() {
	    var data = new google.visualization.DataTable();
	    data.addColumn('string', 'Scenario');
	    data.addColumn('number', 'Scenarios');

	    var resultPassed = 0;
	    var resultFailed = 0;
	    var resultUndefined = 0;
	    var resultSkipped = 0;

	    for(var feature = 0; feature < formatterSummary.length; feature++) {
	        for(var scenario = 0; scenario < formatterSummary[feature].scenarios.length; scenario++) {
				for(var step = 0; step < formatterSummary[feature].scenarios[scenario].steps.length; step++) {
					if (formatterSummary[feature].scenarios[scenario].steps[step].result != null) {
						switch(formatterSummary[feature].scenarios[scenario].steps[step].result.status) {
							case "passed":
								resultPassed++;
								break;
							case "failed":
								resultFailed++;
								break;
							case "undefined":
								resultUndefined++;
								break;
							case "skipped":
								resultSkipped++;
								break;
						}
					}
				}
			}
		}

	    data.addRows([
	    ['Passed', resultPassed],
	    ['Failed', resultFailed],
	    ['Undefined', resultUndefined],
	    ['Skipped', resultSkipped]
    ]);
    return data;
}

function getScenarioData() {
	    var data = new google.visualization.DataTable();
	    data.addColumn('string', 'Scenario');
	    data.addColumn('number', 'Scenarios');

	    var resultPassed = 0;
	    var resultFailed = 0;
	    var resultUndefined = 0;
	    var resultSkipped = 0;

	    for(var feature = 0; feature < formatterSummary.length; feature++) {
	        for(var scenario = 0; scenario < formatterSummary[feature].scenarios.length; scenario++) {
				var scenarioResult = null;
				for(var step = 0; step < formatterSummary[feature].scenarios[scenario].steps.length; step++) {
					if (formatterSummary[feature].scenarios[scenario].steps[step].result != null) {
						switch(formatterSummary[feature].scenarios[scenario].steps[step].result.status) {
							case "passed":
								if (scenarioResult == null) {
									scenarioResult = "passed";
								}
								break;
							case "failed":
								scenarioResult = "failed";
								break;
							case "undefined":
								if (scenarioResult == null || scenarioResult != "failed") {
									scenarioResult = "undefined";
								}
								break;
							case "skipped":
								if (scenarioResult == null || scenarioResult == "passed") {
									scenarioResult = "skipped";
								}
								break;
						}
					}
				}
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
					case "skipped":
						resultSkipped++;
						break;
				}
			}
		}

	    data.addRows([
	    ['Passed', resultPassed],
	    ['Failed', resultFailed],
	    ['Undefined', resultUndefined],
	    ['Skipped', resultSkipped]
    ]);
    return data;
}

function getFeatureData() {
	    var data = new google.visualization.DataTable();
	    data.addColumn('string', 'Scenario');
	    data.addColumn('number', 'Scenarios');

	    var resultPassed = 0;
	    var resultFailed = 0;
	    var resultUndefined = 0;
	    var resultSkipped = 0;

	    for(var feature = 0; feature < formatterSummary.length; feature++) {
			var featureResult = null;
	        for(var scenario = 0; scenario < formatterSummary[feature].scenarios.length; scenario++) {
				for(var step = 0; step < formatterSummary[feature].scenarios[scenario].steps.length; step++) {
					if (formatterSummary[feature].scenarios[scenario].steps[step].result != null) {
						switch(formatterSummary[feature].scenarios[scenario].steps[step].result.status) {
							case "passed":
								if (featureResult == null) {
									featureResult = "passed";
								}
								break;
							case "failed":
								featureResult = "failed";
								break;
							case "undefined":
								if (featureResult == null || featureResult != "failed") {
									featureResult = "undefined";
								}
								break;
							case "skipped":
								if (featureResult == null || featureResult == "passed") {
									featureResult = "skipped";
								}
								break;
						}
					}
				}
			}
			switch(featureResult) {
				case "passed":
					resultPassed++;
					break;
				case "failed":
					resultFailed++;
					break;
				case "undefined":
					resultUndefined++;
					break;
				case "skipped":
					resultSkipped++;
					break;
			}
		}

	    data.addRows([
	    ['Passed', resultPassed],
	    ['Failed', resultFailed],
	    ['Undefined', resultUndefined],
	    ['Skipped', resultSkipped]
    ]);
    return data;
}

function drawPieChart(data, elementId, title) {
    var options = {
        title: title,
        pieSliceText: 'label',
        width: 500,
        height: 500,
        slices: {
            0: { color: '#b0d633' },
            1: { color: '#ed2525' },
            2: { color: '#eaec2d' },
            3: { color: '#00b7b7' }
        },
        titleTextStyle: {
            fontSize: '18',
            bold: true
        }
    };

    new google.visualization.PieChart(document.getElementById(elementId)).draw(data, options);
}
