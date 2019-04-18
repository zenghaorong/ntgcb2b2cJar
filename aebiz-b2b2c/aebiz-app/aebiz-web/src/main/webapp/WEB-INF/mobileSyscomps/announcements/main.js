/**
 * Created by Xic on 2017/6/3.
 */
Comps.announcements = function () {
    this.ajaxUrl = ContextPath + '/comps/integration/getHotBulletinsByUuids4Mobile';
    this.noticeUuid = Utils.getQueryString("noticeUuid");
};
Comps.announcements.prototype.init = function () {
    var _self = this;

    $.ajax({
        type: "POST",
        url: _self.ajaxUrl,
        dataType: "json",
        data:{
            uuids:_self.noticeUuid
        },
        async: true,
        success: function (data) {
            console.log(data.actionType);
            var content = JSON.parse(data[0].actionType);
            template.config('escape', false);
            var html = template('announcements', content);
            $('#content').html(html);
        }

    })
};