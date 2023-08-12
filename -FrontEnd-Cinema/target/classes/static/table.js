var table = undefined;

function getLink() {
    return $('get-link').attr('value');
}

function closeWindow() {
    $('.window').fadeOut(200);
}

function openWindow(windowName) {
    $(`.window.${windowName}`).fadeIn(200);
}

function getTable() {
    var sortBy = $('input[name="sort"]:checked').val();
    var sortValue = $('select[name="sort__value"]').val();

    var filterBy = $('input[name="filter"]:checked').val();
    var filterValue = $('input[name="filter__value"]').val();

    $.ajax({
        type: "GET",
        url: getLink(),
        data: jQuery.param({ 'sortBy': sortBy, 'sortValue': sortValue, 'filterBy': filterBy, 'filterValue': filterValue }),
        complete: function (response) {
            table = JSON.parse(response.responseText);
            showTable();
        }
    });
}

function getTableRow(valueOfElement) {
    var tr = document.createElement('tr');
    $('thead th').each(function (index, element) {
        var tdClassName = $(element).attr('class');
        var td = document.createElement(`td`);
        td.className = tdClassName;
        td.textContent = valueOfElement[tdClassName]
        tr.appendChild(td);
    });

    return tr.outerHTML;
}

function showTable() {
    $('tbody').html('');
    $.each(table, function (indexInArray, valueOfElement) { 
        var tableRowHTML = getTableRow(valueOfElement);
    //      $('tbody').append(`
    //      <tr>
    //      <td class="id">${valueOfElement.id}</td>
    //      <td class="fullName">${valueOfElement.fullName}</td>
    //      <td class="age">${valueOfElement.age}</td>
    //      <td class="salary">${valueOfElement.salary}</td>
    //      <td class="specialty">${valueOfElement.specialty}</td>
    //      <td class="workExperience">${valueOfElement.workExperience}</td>
    //  </tr>
    //      `);
        $('tbody').append(tableRowHTML);
    });

    if (isAdmin()) {
        $('tbody tr').click(function (e) { 
            $('.window.edit .info__variable input').eq(0).val( $(this).children('td').eq(0).html() );
            $(this).children('td').each(function (index, element) {
                if (index > 0) {
                    $('.window.edit .info__variable input').eq(index).val( '' );
                }
                $('.window.edit .info__variable input').eq(index).attr('placeholder', element.innerHTML);
            });

            openWindow('edit');
        });

        $('.show_add_window').click(function (e) { 
            openWindow('add');
        });
    }
}

function isAdmin() {
    return $('admin').attr('value') == 'true';
}

// function addEditWindow() {
//     $('body').prepend(`
//     <div class="window edit" style="display: none">
//     <div class="background"></div>
//     <div class="content constraint">
//         <div class="window_title">
//             <h2>Редактирование доктора</h2>
//         </div>
//         <div class="info">
//             <div class="info__variable">
//                 <input name="id" required placeholder="ID доктора" type="text" style="display: none">
//             </div>
//             <div class="info__variable">
//                 <input name="fullName" required placeholder="Ф.И.О доктора" type="text">
//                 <span class="floating-label">Ф.И.О доктора</span>
//             </div>
//             <div class="info__variable">
//                 <input name="age" required placeholder="Возраст доктора" type="number" min="1">
//                 <span class="floating-label">Возраст доктора</span>
//             </div>
//             <div class="info__variable">
//                 <input name="salary" required placeholder="Зарплата доктора" type="number" min="0">
//                 <span class="floating-label">Зарплата доктора</span>
//             </div>
//             <div class="info__variable">
//                 <input name="specialty" required placeholder="Специальность доктора" type="text">
//                 <span class="floating-label">Специальность доктора</span>
//             </div>
//             <div class="info__variable">
//                 <input name="workExperience" required placeholder="Опыт работы доктора" type="number" min="0">
//                 <span class="floating-label">Опыт работы доктора</span>
//             </div>
//         </div>
//         <div class="window_remote">
//             <button name="delete">Удалить</button>
//             <button name="edit">Изменить</button>
//         </div>
//     </div>
// </div>
//     `);

    
// }

$(document).ready(function () {
    getTable();
    showTable();

    // Закрытие при нажатии на задний фон
    $('.background').click(function (e) { 
        closeWindow(); $('button[class="show_statistic_btn"]').fadeIn(200); 
     });

    if (isAdmin()) {
        $('<div class="show_add_window">Добавить</div>').insertBefore('table');

        // Нажатие на кнопку удаления
        $('button[name="delete"]').click(function (e) { 
            $.ajax({
                type: "DELETE",
                url: getLink(),
                data: jQuery.param({'id': $('.window.edit .info__variable input').eq(0).val()}),
                success: function(result) {
                    getTable(); showTable(); closeWindow(); getStatistic();
                }
            }); 
        });

        // Нажатие на кнопку изменения
        $('button[name="edit"]').click(function(e) {
            var params = {};
            $('.info__variable input').each(function (index, element) {
                if (element.value != '') params[element.name] = element.value;
            });

            $.ajax({
                type: "PUT",
                url: getLink(),
                data: params,
                success: function (response) {
                    getTable(); showTable(); getStatistic();
                },

                error: function (response) {
                    alert(response.responseText);
                }
            });
        })

        // Нажатие на кнопку добавления
        $('button[name="add"]').click(function (e) { 
            var params = {};
            $('.info__variable input').each(function (index, element) {
                if (element.name != 'id' && element.value != '') {
                    params[element.name] = element.value;
                }
            });

            $.ajax({
                type: "POST",
                url: getLink(),
                data: params,
                success: function (response) {
                    getTable(); showTable(); getStatistic();
                },

                error: function (response) {
                    alert(response.responseText);
                }
            });
        });
    }
});

// При изменении одного из параметров пересобрать таблицу
$('input[name="sort"], select[name="sort__value"], input[name="filter"]').on('input', function () {
    getTable();
    showTable();
});

// При отведении курсора от "Значение поиска" пересобрать таблицу
$('input[name="filter__value"]').focusout(function() {
    getTable();
    showTable();
})

// При нажатии 'ENTER' на поле filter__value пересобрать таблицу
$('input[name="filter__value"]').keyup(function (e) { 
    if (e.keyCode == 13) {
        getTable();
        showTable();
    }
});