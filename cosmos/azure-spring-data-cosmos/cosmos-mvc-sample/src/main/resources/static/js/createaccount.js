$(function() {

  $("form[name='create-account']").validate({
    // validation rules
    rules: {
      firstName: "required",
      lastName: "required",
      email: {
              required: true,

              email: true
            },
      password: {
        required: true,
        minlength: 5
      },
      confirmPassword : {
         required: true,
         equalTo : "#password"
      },

    },
            errorPlacement: function(error, element) {
               if(element.parent('.input-group').length) {
                   error.insertAfter(element.parent());
               } else {
                   error.insertAfter(element);
               }
           },
    // validation error messages
    messages: {
      firstName: "Please enter your first name",
      lastName: "Please enter your last name",
      email: "Please enter a valid email address",
      password: {
        required: "Please enter your password",
        minlength: "Password must be at least 5 characters"
      },
      confirmPassword: {
         required: "Please re-enter your password",
         equalTo: "Your passwords must match"
            },
    },
    submitHandler: function(form) {
      form.submit();
    }
  });
});