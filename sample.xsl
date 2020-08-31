<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="text" encoding="UTF-8"/>
    <xsl:template match="/root">
        <xsl:text>transactionIdentifier,employerNames,employerABN,fundIdentifier,fundEmployerIdentifier,memberFirstName,memberLastName,memberOtherNames,memberDateOfBirth,memberGender,memberAddress,memberEmail,memberContactNumber,memberNumber,memberTFN,memberPayrollNumber,memberEmploymentStatus,memberFundRegistrationDate</xsl:text>
        <xsl:for-each select="//row">
            <xsl:value-of select="concat(transactionIdentifier, ',' ,employerNames, ',' ,employerABN,',',fundIdentifier, ',' ,fundEmployerIdentifier,',' ,memberFirstName, ',' ,memberLastName,',',memberOtherNames, ',' , memberDateOfBirth,',' ,memberGender, ',' ,memberAddress,',' ,memberEmail, ',' ,memberContactNumber, ',' ,memberNumber, ',' ,memberTFN,',',memberPayrollNumber, ',' ,memberEmploymentStatus,',' ,memberFundRegistrationDate,'&#xA;')"/>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>
