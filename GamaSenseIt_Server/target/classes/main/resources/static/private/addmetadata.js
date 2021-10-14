i = 0 ;

openItem = -1;
function addElements()
{
	$('#table').bootstrapTable('insertRow',{
	    index: i,
		row: {
	        id: i,
	        name: i,
	        price: "<a onclick='openRow("+i+");'>open</a>"
	    }
	});
	i = i + 1;
	

}

function closeRow(mi)
{
	$('#table').bootstrapTable('updateRow',{
	    index: mi,
		row: {
	        id:  $('#modifyID').val(),
	        name: $('#modifyName').val(),
	        price: "<a onclick='openRow("+mi+");'>open</a>"
	    }
	});
}

function openRow(mi)
{
	if(openItem != -1)
	{
		closeRow(openItem);
	}
	var data  = $('#table').bootstrapTable('getData')[mi];
	
	$('#table').bootstrapTable('updateRow',{
	    index: mi,
		row: {
	        id:  "<input type='text' name='modifyID' id='modifyID' value='"+data.id+"'/>",
	        name: "<input type='text' name='modifyName' id='modifyName' value='"+data.name+"'/>",
	        price: "<a onclick='openRow("+mi+");'>open</a>"
	    }
	});
	openItem = mi;
	
}