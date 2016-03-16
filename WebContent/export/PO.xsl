<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
  <html>
	<head>
		<title>Purchase Order Confirmation</title>
	</head>
  <body>

  <h4> 	 
  	<p>Order id: <xsl:value-of select="order/@id"/></p>
  	<p>Name: <xsl:value-of select="order/customer/name"/></p>
	<p>Account: <xsl:value-of select="order/customer/@account"/></p>
	<p>Date Submitted: <xsl:value-of select="order/@submitted"/></p>
  </h4> 

  <hr />

   <table border="1" cellpadding="3">
   	<tr>
   	<th>Number</th>
   	<th>Name</th>
   	<th>Price</th>
   	<th>Quantity</th>
   	<th>Extended</th>
   	</tr>

   	<xsl:for-each select="order/items/item">
    <tr>
      	<td><xsl:value-of select="@number"/></td>
        <td><xsl:value-of select="name"/></td>
        <td>$<xsl:value-of select="price"/></td>
        <td><xsl:value-of select="quantity"/></td>
        <td>$<xsl:value-of select="extended"/></td>
    </tr>
	</xsl:for-each>

	</table>

	<hr />
    <h4> 
    	<p>Total: $<xsl:value-of select="order/total"/></p>
		<p>Shipping: $<xsl:value-of select="order/shipping"/></p>
		<p>Taxes: $<xsl:value-of select="order/HST"/></p>
		<p>Grand Total: $<xsl:value-of select="order/grandTotal"/></p>
	</h4>

  </body>
  </html>
  
</xsl:template>

</xsl:stylesheet>