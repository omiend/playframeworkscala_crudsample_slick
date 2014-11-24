// Some general UI pack related JS
// Extend JS String with repeat method
String.prototype.repeat = function (num) {
  return new Array(num + 1).join(this);
};

(function ($) {
  $(function () {

    // Custom Selects
    if ($('[data-toggle="select"]').length) {
      $('[data-toggle="select"]').select2();
    }

    // make code pretty
    window.prettyPrint && prettyPrint();
  });

})(jQuery);
