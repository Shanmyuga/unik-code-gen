<!DOCTYPE html>
<head>
	<title>Admin Portal | Home </title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	
	<script type="application/x-javascript"> 
		addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); 
		function hideURLbar() { 
			window.scrollTo(0,1); 
		} 
	</script>
	
	<link href='http://fonts.googleapis.com/css?family=Roboto:400,100,100italic,300,300italic,400italic,500,500italic,700,700italic,900,900italic' rel='stylesheet' type='text/css'>
	<link href="src/assets/css/bootstrap.css" rel="stylesheet" />
	<link href="src/assets/css/style-admin.css" rel='stylesheet' type='text/css' />
	<link href="src/assets/css/font.css" rel="stylesheet" type="text/css"/>
	<link href="src/assets/css/font-awesome.css" rel="stylesheet">
	<link href="src/assets/css/colored-admin.css" rel="stylesheet"> 

	<script src="src/assets/js/jquery2.0.3.min.js"></script>
	<script src="src/assets/js/screenfull.js"></script>
	<script src="src/assets/js/jquery.cookie.js"></script>
	<script src="src/assets/js/modernizr.js"></script>
	
	<script>
		$(function () {
			$('#supported').text('Supported/allowed: ' + !!screenfull.enabled);

			if (!screenfull.enabled) {
				return false;
			}

			

			$('#toggle').click(function () {
				screenfull.toggle($('#container')[0]);
			});	
		});
	</script>
</head>

<body class="dashboard-page">
	<script>
        var theme = $.cookie('protonTheme') || 'default';
        $('body').removeClass (function (index, css) {
            return (css.match (/\btheme-\S+/g) || []).join(' ');
        });
        if (theme !== 'default') $('body').addClass(theme);
    </script>
    <div id="rootWrapper"></div>
	<script src="./src/main.js"></script>

	<script src="src/assets/js/bootstrap.js"></script>
	<script src="src/assets/js/proton.js"></script>
</body>
</html>	