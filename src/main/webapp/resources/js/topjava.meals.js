var mealUrl = "profile/meals/";

// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: mealUrl,
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "dateTime"
                    },
                    {
                        "data": "description"
                    },
                    {
                        "data": "calories"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "desc"
                    ]
                ]
            }),
        update: filter
        }
    );
});

function filter() {
    $.ajax({
        type: "GET",
        url: mealUrl + "filter",
        data: $("#filterForm").serialize()
    }).done(updateTableWithData);
}

function clearFilter() {
    $("#filterForm")[0].reset();
    $.get(mealUrl, updateTableWithData);
}
