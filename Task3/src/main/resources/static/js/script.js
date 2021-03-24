var domen;

$(document).ready(function(){
    domen = "http://localhost:8080";
    updateBookList();
    updateLanguageList();
    updateLocalizationList();
    updateLocalizationForm();
});

function updateBookList(){
	var booksBlock = $("#booksBlock");
	booksBlock.empty();
	$.get(domen+"/books", function(response){
        $.each(response, function(index, book){
        	booksBlock.append($("<tr>"));
            $("<td>").text(book.id).appendTo(booksBlock);
            $("<td>").text(book.name).appendTo(booksBlock);
			booksBlock.append(
				$("<td><button type=\"submit\" onclick=\"deleteBook("+book.id+")\" class=\"btn btn-danger\">Delete</button></td>"));
			booksBlock.append($("</tr>"));
        });
    });
}

function updateLanguageList(){
	var langBlock = $("#langBlock");
	langBlock.empty();
	$.get(domen+"/languages", function(response){
        $.each(response, function(index, lang){
        	langBlock.append($("<tr>"));
            $("<td>").text(lang.id).appendTo(langBlock);
            $("<td>").text(lang.abbreviation).appendTo(langBlock);
			langBlock.append(
			$("<td><button onclick=\"deleteLanguage("+lang.id+")\" class=\"btn btn-danger\">Delete</button>"+
			"<button onclick=\"showBooks(\'"+lang.abbreviation+"\')\" class=\"btn btn-primary\">Show books</button></td>"));
        	langBlock.append($("</tr>"));
        });
    });
}

function updateLocalizationList(){
	var localsBlock = $("#localsBlock");
	localsBlock.empty();
	$.get(domen+"/localizations", function(response){
        $.each(response, function(index, local){
        	localsBlock.append($("<tr>"));
            $("<td>").text(local.id).appendTo(localsBlock);
            $("<td>").text(local.book.name).appendTo(localsBlock);
            $("<td>").text(local.language.abbreviation).appendTo(localsBlock);
            $("<td>").text(local.value).appendTo(localsBlock);
			localsBlock.append(
			$("<td><button onclick=\"deleteLocalization("+local.id+")\" class=\"btn btn-danger\">Delete</button></td>"));
        	localsBlock.append($("</tr>"));
        });
    });
}

function updateLocalizationForm(){
	var bookRadioList = $("#bookRadioList");
	bookRadioList.empty();
	$.get(domen+"/books", function(response){
		$.each(response, function(index, book){
			bookRadioList.append(
			$("<input type=\"radio\" name=\"bookId\" value=\""+book.id+"\"/>"+book.name+"<br/>"));
    	});
    });
    var langRadioList = $("#langRadioList");
    langRadioList.empty();
    $.get(domen+"/languages", function(response){
		$.each(response, function(index, lang){
			langRadioList.append(
			$("<input type=\"radio\" name=\"langId\" value=\""+lang.id+"\"/>"+lang.abbreviation+"<br/>"));
    	});
    });
}

function addLocal(){
	var radios = document.getElementsByName('bookId');
	var radios2 = document.getElementsByName('langId');
	var bookId, langId;
	for (var i = 0, length = radios.length; i < length; i++) {
  		if (radios[i].checked) {
    		bookId=radios[i].value;
    		break;
  		}
	}
	for (var i = 0, length = radios2.length; i < length; i++) {
  		if (radios2[i].checked) {
    		langId=radios2[i].value;
    		break;
  		}
	}
	var value = $("#localizationValueInput").val();
	if(bookId!=null & langId!=null & value!=""){
		$.post(domen+"/localizations", {bookId: bookId, langId: langId, value: value}, function(){
         	updateLocalizationList();
		}).fail(function(){
      		console.log("error");
      	});
	}
}

function deleteRecord(url){
	$.ajax({
        url: url,
        type: 'DELETE',
        success: function(result) {
            window.location.replace(domen);
        }
    });
}
function deleteBook(id){
    deleteRecord(domen+"/books?id="+id);
    updateBookList();
}
function deleteLanguage(id){
	deleteRecord(domen+"/languages?id="+id);
	updateLanguageList();
}
function deleteLocalization(id){
	deleteRecord(domen+"/localizations?id="+id);
	updateLocalizationList();
}

function addBook(){
	var input = $("#bookNameInput");
	bookName = input.val();
    $.post(domen+"/books", {name: bookName}, function(){
         updateBookList();
	}).fail(function(){
      	console.log("error");
});
}

function addLang(){
	var input = $("#languageAbbInput");
	abbreviation = input.val();
    $.post(domen+"/languages", {abbreviation: abbreviation}, function(){
         updateLanguageList();
	}).fail(function(){
      	console.log("error");
});
}

function showBooks(lang){
	var booksBlock = $("#booksBlock");
	booksBlock.empty();
	$.get(domen+"/books/"+lang, function(response){
        $.each(response, function(index, book){
        	booksBlock.append($("<tr>"));
            $("<td>").text(book.id).appendTo(booksBlock);
            $("<td>").text(book.name).appendTo(booksBlock);
			booksBlock.append(
			$("<td><button type=\"submit\" onclick=\"deleteBook("+book.id+")\" class=\"btn btn-danger\">Delete</button></td>"));
        	booksBlock.append($("</tr>"));
        });
    });
}