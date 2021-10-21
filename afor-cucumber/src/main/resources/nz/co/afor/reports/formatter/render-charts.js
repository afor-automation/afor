google.charts.load('current', {packages: ['corechart', 'table']});
var scenarioBreakdownData;
var scenarioDurationData;

google.charts.setOnLoadCallback(drawScenarioDataCharts);
google.charts.setOnLoadCallback(drawScenarioDurationCharts);

$(window).on("throttledresize", function (event) {
    drawCharts();
});

function drawCharts() {
    drawScenarioDataCharts();
    drawScenarioDurationCharts();
}

function drawScenarioDataCharts() {
	writeBreakdown('scenarioSummaryBreakdown');
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

function getScenarioDurationData() {
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'Scenario');
    data.addColumn('number', 'Scenario duration');

    for(var scenario = 0; scenario < formatterPerformanceSummary.length; scenario++) {
        data.addRow([formatterPerformanceSummary[scenario].name, formatterPerformanceSummary[scenario].durationInSeconds]);
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
            title: vAxisTitle,
            titleTextStyle: {italic: false, fontSize: '14', fontName: 'Helvetica, Arial, sans-serif'}
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
	element.innerHTML = "<div class=\"breakdownRow\">" + getTotalFeatureCount() + " <b>Features</b> (<span class=\"passed\">" + formatterHighLevelSummary.features.passed + " passed,</span>	 <span class=\"failed\">" + formatterHighLevelSummary.features.failed + " failed,</span> <span class=\"undefined\">" + formatterHighLevelSummary.features.undefined + " undefined,</span> <span class=\"pending\">" + formatterHighLevelSummary.features.pending + " pending</span>)</div>";
	element.innerHTML += "<div class=\"breakdownRow\">" + getScenarioCount() + " <b>Scenarios</b> (<span class=\"passed\">" + formatterHighLevelSummary.scenarios.passed + " passed,</span> <span class=\"failed\">" + formatterHighLevelSummary.scenarios.failed + " failed,</span> <span class=\"undefined\">" + formatterHighLevelSummary.scenarios.undefined + " undefined,</span> <span class=\"pending\">" + formatterHighLevelSummary.scenarios.pending + " pending</span>)</div>";
	element.innerHTML += "<div class=\"breakdownRow\">" + getStepCount() + " <b>Steps</b> (<span class=\"passed\">" + formatterHighLevelSummary.steps.passed + " passed,</span> <span class=\"failed\">" + formatterHighLevelSummary.steps.failed + " failed,</span> <span class=\"undefined\">" + formatterHighLevelSummary.steps.undefined + " undefined,</span> <span class=\"pending\">" + formatterHighLevelSummary.steps.pending + " pending,</span> <span class=\"skipped\">" + formatterHighLevelSummary.steps.skipped + " skipped</span>  <span class=\"skipped\">" + formatterHighLevelSummary.steps.ambiguous + " ambiguous</span>)</div>";
}

// Feature table breakdown
var loadedFeatures = 0;
$(window).load(function() {
    loadFeatureBreakdown();
    loadMoreFeatureBreakdownRows();
});

$(window).scroll(function() {
    if($('#featureScenarioContainer').scrollTop() + $('#featureScenarioContainer').innerHeight() >= $('#featureScenarioContainer')[0].scrollHeight) {
        loadMoreFeatureBreakdownRows();
    }
});

function loadFeatureBreakdown() {
    var hasExtendedFailures = (formatterHighLevelSummary.scenarios.undefined + formatterHighLevelSummary.scenarios.pending + formatterHighLevelSummary.scenarios.skipped + formatterHighLevelSummary.scenarios.ambiguous) > 0;
    var tableHeader = "<table id='featureScenarioTable'><thead><tr class=\"headerRow\"><th class=\"headerCell\">Name</th><th>Passed</th><th class=\"headerCell\">Failed</th>";
    if (hasExtendedFailures) {
        tableHeader += "<th class=\"headerCell\">Undefined</th><th class=\"headerCell\">Pending</th><th class=\"headerCell\">Skipped</th><th class=\"headerCell\">Ambiguous</th>";
    }
    tableHeader += "</tr></thead></table>";
    $('#featureScenarioContainer').append(tableHeader);
}

function loadMoreFeatureBreakdownRows() {
    var hasExtendedFailures = (formatterHighLevelSummary.scenarios.undefined + formatterHighLevelSummary.scenarios.pending + formatterHighLevelSummary.scenarios.skipped + formatterHighLevelSummary.scenarios.ambiguous) > 0;
    if (formatterFeatureSummary.length > loadedFeatures) {
        for(var maxFeature = loadedFeatures + 25; loadedFeatures < maxFeature && loadedFeatures < formatterFeatureSummary.length; loadedFeatures++) {
            var row = document.getElementById("featureScenarioTable").insertRow(-1);
            row.classList.add("tableRow");
            var name = row.insertCell(0);
            name.innerHTML = "<a href=\"#" + formatterFeatureSummary[loadedFeatures].href + "\">" + formatterFeatureSummary[loadedFeatures].name + "</a>";
            var passed = row.insertCell(1);
            passed.innerHTML = formatterFeatureSummary[loadedFeatures].scenarioResults.passed;
            var failed = row.insertCell(2);
            failed.innerHTML = formatterFeatureSummary[loadedFeatures].scenarioResults.failed;
            if (hasExtendedFailures) {
                var undefined = row.insertCell(3);
                undefined.innerHTML = formatterFeatureSummary[loadedFeatures].scenarioResults.undefined;
                var pending = row.insertCell(4);
                pending.innerHTML = formatterFeatureSummary[loadedFeatures].scenarioResults.pending;
                var skipped = row.insertCell(5);
                skipped.innerHTML = formatterFeatureSummary[loadedFeatures].scenarioResults.skipped;
                var ambiguous = row.insertCell(6);
                ambiguous.innerHTML = formatterFeatureSummary[loadedFeatures].scenarioResults.ambiguous;
            }
        }
    }
    document.getElementById("featureScenarioContainer").style.display = "block";
}

$.extend($.expr[':'], {
  'containsi': function(elem, i, match, array) {
    return (elem.textContent || elem.innerText || '').toLowerCase()
        .indexOf((match[3] || "").toLowerCase()) >= 0;
  }
});

var delayTimer;
function searchFunction() {
        clearTimeout(delayTimer);
        if ($("input[name='keyword']").val().length == 0) {
            $("section.feature").show();
            $("section.scenario").show();
            if (undefined == $("input[name='passed']").attr('checked')) {
                $("section.scenario.passed").hide();
            }
            if (undefined == $("input[name='failed']").attr('checked')) {
                $("section.scenario.failed").hide();
            }
            if (undefined == $("input[name='other']").attr('checked')) {
                $("section.scenario.ambiguous").hide();
                $("section.scenario.pending").hide();
                $("section.scenario.skipped").hide();
                $("section.scenario.undefined").hide();
            }
            filter();
        } else {
            delayTimer = setTimeout(function() {
                filter();
            }, 1000);
        }
}

function togglePassedFunction() {
    if (undefined == $("input[name='passed']").attr('checked')) {
        $("section.scenario.passed").hide();
    } else {
        $("section.scenario.passed").show();
    }
    filter();
}

function toggleFailedFunction() {
    if (undefined == $("input[name='failed']").attr('checked')) {
        $("section.scenario.failed").hide();
    } else {
        $("section.scenario.failed").show();
    }
    filter();
}

function toggleOtherFunction() {
    if (undefined == $("input[name='other']").attr('checked')) {
        $("section.scenario.ambiguous").hide();
        $("section.scenario.pending").hide();
        $("section.scenario.skipped").hide();
        $("section.scenario.undefined").hide();
    } else {
        $("section.scenario.ambiguous").show();
        $("section.scenario.pending").show();
        $("section.scenario.skipped").show();
        $("section.scenario.undefined").show();
    }
    filter();
}

function filter() {
    if ($("input[name='keyword']").val().length > 2) {
        $("section.feature")
            .hide()
            .filter(":containsi('" + $("input[name='keyword']").val() + "')")
            .show();
        $("section.scenario")
            .hide()
            .filter(":containsi('" + $("input[name='keyword']").val() + "')")
            .show();
        if (undefined == $("input[name='passed']").attr('checked')) {
            $("section.scenario.passed").hide();
        }
        if (undefined == $("input[name='failed']").attr('checked')) {
            $("section.scenario.failed").hide();
        }
        if (undefined == $("input[name='other']").attr('checked')) {
            $("section.scenario.ambiguous").hide();
            $("section.scenario.pending").hide();
            $("section.scenario.skipped").hide();
            $("section.scenario.undefined").hide();
        }
    }

    $("section.feature").filter(function() {
        return $(this).find("section.scenario").filter(function() {
            return $(this).css("display") === "block";
        }).length > 0;
    }).show();
    $("section.feature").filter(function() {
        return $(this).find("section.scenario").filter(function() {
            return $(this).css("display") === "block";
        }).length == 0;
    }).hide();
}

function toggleExpandCollapse() {
    if ($("#expandCollapse").text() == "expand all") {
        $("#expandCollapse").text("collapse all");
        $("section details").attr("open", "open");
    } else {
        $("#expandCollapse").text("expand all");
        $("section details").removeAttr("open");
    }
}