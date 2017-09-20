$(document).ready(function(){
	$.ajax({
		url: '/receipts',
		type: 'GET',
		contentType: 'application/json',
		success: function(array){
			array.forEach(function(each){
				var receipt = $("<div class='receipt'></div>");
				receipt.append($("<input type='hidden' value='" + each.id + "'/>"));
				receipt.append($("<div class='time'>" + each.created + "</div>"));
				receipt.append($("<div class='merchant'>" + each.merchantName + "</div>"));
				if(each.amount)
					receipt.append($("<div class='amount'>" + each.value + "</div>"));
				else
					receipt.append($("<div class='amount'></div>"));
				var tags = $("<div class='tags'></div>");
				tags.forEach(function(tag){
					tags.append($("<button class='tag_input' value=''>" + tag + "</div>"));
				});
				receipt.append(tags);
				$('#receiptList').append(receipt);
			});
		}
	});

	$('#add-receipt').click(function(){
		$('#add-receipt-form').toggle();
	});

	$('#save-receipt').click(function(){
		$.ajax({
			url: '/receipts',
			type: 'POST',
			contentType: 'application/json',
			data: $('#add-receipt-form').serializeArray(),
			success: function(result){
				var receipt = $("<div class='receipt'></div>");
				receipt.append($("<input type='hidden' class='id' value='" + result.id + "'/>"));
				receipt.append($("<div class='time'>" + result.created + "</div>"));
				receipt.append($("<div class='merchant'>" + result.merchantName + "</div>"));
				if(result.amount)
					receipt.append($("<div class='amount'>" + result.value + "</div>"));
				else
					receipt.append($("<div class='amount'></div>"));
				var tags = $("<div class='tags'></div>");
				tags.append($("<button class='add-tag'>Add +</div>"));
				tags.forEach(function(tag){
					tags.append($("<button class='tag_toggle' value=''>" + tag + "</div>"));
				});
				receipt.append(tags);
				$('#receiptList').append(receipt);
			}
		});
	});

	$('#cancel-receipt').click(function(){
		$('#merchant').val("");
		$('#amount').val("");
		$('#add-receipt-form').toggle();
	});

	$('.tag_toggle').click(function(){
		$.ajax({
			url: '/tags/' + $(this).text(),
			type: 'PUT',
			contentType: 'application/json',
			data: $('.id', $(this).parent().parent()).value
			success: function(){
				$(this).remove();
			}
		});
	});

	$('.add-tag').click(function(){
		$(this).parent().append($("<input type='text' class='tag_input' required='true'"));
	});

	$('.tag_input').live("keypress", function(e){
		if(e.keyCode == 13){
			$.ajax({
				url: '/tags/' + this.val(),
				type: 'PUT',
				contentType: 'application/json',
				success: function(){
					$(this).parent().append($("<button class='tag_toggle' value='" + tag + "'>" + tag + "</div>"));
					$(this).remove();
				}
			})
		}
	});
});