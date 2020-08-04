var userUrl = "admin/users/";

// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: userUrl,
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "name"
                    },
                    {
                        "data": "email"
                    },
                    {
                        "data": "roles"
                    },
                    {
                        "data": "enabled"
                    },
                    {
                        "data": "registered"
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
                        "asc"
                    ]
                ]
            }),
            update: updateTable
        }
    );
});

function enableUser(checkbox) {
    let id = checkbox.parent().parent().attr("data-id");
    let enabled = checkbox.is(":checked");
    $.ajax({
        type: "POST",
        url: userUrl + id,
        data: "enabled=" + enabled
    }).done(function () {
        checkbox.closest("tr").attr("data-userEnabled", enabled);
        successNoty(enabled ? "User is enabled" : "User is disabled");
    }).fail(function () {
        checkbox.prop("checked", !enabled);
    });
}