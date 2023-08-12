var graph = undefined;
const ctx = document.getElementById('myChart');

function getURL() {
    return $('statistic-get').attr('href');
}

function getStatistic() {
    $.ajax({
        type: "GET",
        url: getURL(),
        dataType: "dataType",
        complete: function (response) {
            if (response.status == 200) {
                renderGraph(
                    JSON.parse( response.responseText )
                );
            }
        }
    });
}

function renderGraph(statistic) {
    graph = new Chart(ctx, {
        type: statistic.type,
        data: {
          labels: statistic.labels,
          datasets: statistic.datasets
        },
        options: {
          scales: {
            y: {
              beginAtZero: true
            }
          }
        }
      });
}

$('button[class="show_statistic_btn"]').click(function (e) { 
    $(this).fadeOut(200);
    $('.window.statistic').fadeIn(200);
});

$(document).ready(function () {
   getStatistic();
});