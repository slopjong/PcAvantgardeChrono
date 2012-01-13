function onPageLoad()
{
    detectBrowser();
    fixupIEPNGBG("id1");
    fixupIEPNG("id2", "Welcome_files/transparent.gif");
    fixupIEPNG("id3", "Welcome_files/transparent.gif");
    adjustPositioningIfWrongSize("id4", 50, 1);
    fixupIEPNG("id5", "Welcome_files/transparent.gif");
    return true;
}

var windowsInternetExplorer = false;
function detectBrowser()
{
    windowsInternetExplorer = false;
    var appVersion = navigator.appVersion;
    if ((appVersion.indexOf("MSIE") != -1) &&
        (appVersion.indexOf("Macintosh") == -1))
    {
        windowsInternetExplorer = true;
    }
}

var smallTransparentGif = "";
function fixupIEPNG(strImageID, transparentGif) 
{
    smallTransparentGif = transparentGif;
    if (windowsInternetExplorer)
    {
        var img = document.getElementById(strImageID);
        if (img)
        {
            var src = img.src;
            img.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='" + src + "', sizingMethod='scale')";
            img.src = transparentGif;
            img.attachEvent("onpropertychange", imgPropertyChanged);
        }
    }
}

function fixupIEPNGBG(strImageID) 
{
    if (windowsInternetExplorer)
    {
        var oBlock = document.getElementById(strImageID);
        if (oBlock)
        {
            var currentBGStyle = oBlock.style.background;
            var urlStart = currentBGStyle.indexOf("url(");
            var urlEnd = currentBGStyle.indexOf(")", urlStart);
            var imageURL = currentBGStyle.substring(urlStart + 4, urlEnd);
            var filterStyle =
                "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='" +
                imageURL +
                "', sizingMethod='crop');";

            oBlock.style.filter = filterStyle;
            oBlock.style.background = "";
        }
    }
}

var inImgPropertyChanged = false;
function imgPropertyChanged()
{
    if ((window.event.propertyName == "src") && (! inImgPropertyChanged))
    {
        inImgPropertyChanged = true;
        var el = window.event.srcElement;
        if (el.src != smallTransparentGif)
        {
            el.filters.item(0).src = el.src;
            el.src = smallTransparentGif;
        }
        inImgPropertyChanged = false;
    }
}

function getChildOfType(oParent, sNodeName, requestedIndex)
{
    var index = 0;
    for (i = 0; i < oParent.childNodes.length; i++)
    {
        if (oParent.childNodes[i].nodeName == sNodeName)
        {
            if (index == requestedIndex)
            {
                return oParent.childNodes[i];
            }
            else
            {
                index++;
            }
        }
    }
    return null;
}

function adjustPositioningIfWrongSize(idOfElement, shrinkPadding, shrink)
{
    var oTextBoxOuterDiv;
    var oTextBoxMiddleDiv;
    var oTextBoxInnerDiv;
    var oTextBoxAlternateImage;
    var oImg;
    var adjustPositioning = false;
    var adjustmentPixels = 0;
    var oTextBoxOuterDiv = document.getElementById(idOfElement);
    if (oTextBoxOuterDiv)
    {
        oTextBoxMiddleDiv = getChildOfType(oTextBoxOuterDiv, "DIV", 0);
        if (oTextBoxMiddleDiv)
        {
            oTextBoxInnerDiv = getChildOfType(oTextBoxMiddleDiv, "DIV", 0);
            if (oTextBoxInnerDiv)
            {
                var oCachedHeight;
                if (windowsInternetExplorer)
                {
                    oCachedHeight = oTextBoxInnerDiv.style.height;
                    oTextBoxInnerDiv.style.height = "100px";
                }
                var clientHeight = oTextBoxInnerDiv.clientHeight;
                var specifiedHeight = clientHeight;
                if (oTextBoxMiddleDiv.style.height != "")
                {
                    specifiedHeight = parseFloat(oTextBoxMiddleDiv.style.height);
                }
                else if (oTextBoxOuterDiv.style.height != "")
                {
                    specifiedHeight = parseFloat(oTextBoxOuterDiv.style.height);
                }
                if ((windowsInternetExplorer) && (clientHeight == 100))
                {
                    clientHeight = specifiedHeight;
                }
                if (clientHeight > specifiedHeight || 
                    specifiedHeight > clientHeight + shrinkPadding )
                {
                    adjustPositioning = true;
                    adjustmentPixels = clientHeight - specifiedHeight;
                    if (specifiedHeight > clientHeight)
                    {
                      if (! shrink)
                      {
                          adjustPositioning = false;
                      }
                      else
                      {
                          adjustmentPixels += shrinkPadding;
                      }
                      
                    }
                }
                if (windowsInternetExplorer)
                {
                    oTextBoxInnerDiv.style.height = oCachedHeight;
                }
            }
        }
    }
    
    if (adjustPositioning)
    {
        var currentOuterHeight = parseInt(oTextBoxOuterDiv.style.height);
        var newOuterHeight = currentOuterHeight + adjustmentPixels;
        if( newOuterHeight <= 0 )        {            newOuterHeight = 1;        }        oTextBoxOuterDiv.style.height = "" + newOuterHeight + "px";

        oFooterLayer = document.getElementById("footer_layer");
        if (oFooterLayer)
        {
            var currentYPos = parseInt(oFooterLayer.style.top);
            var newYPos = currentYPos + adjustmentPixels;
            oFooterLayer.style.top = "" + newYPos + "px";
        }

        oBodyLayer = document.getElementById("body_layer");
        if (oBodyLayer)
        {
            var currentBodyLayerHeight = parseInt(oBodyLayer.style.height);
            var newBodyLayerHeight = currentBodyLayerHeight + adjustmentPixels;
            oBodyLayer.style.height = "" + newBodyLayerHeight + "px";
        }

        oBodyContentDiv = document.getElementById("bodyContent");
        if (oBodyContentDiv)
        {
            var currentBodyContentHeight = parseInt(oBodyContentDiv.style.height);
            var newBodyContentHeight = currentBodyContentHeight + adjustmentPixels;
            oBodyContentDiv.style.height = "" + newBodyContentHeight + "px";
        }

        oNavLayer = document.getElementById("nav_layer");
        if(oNavLayer)
        {
            var currentNavLayerHeight = parseInt(oNavLayer.style.height);
            var diff = currentNavLayerHeight - currentBodyContentHeight;
            if ((diff > -10) && (diff < 10))
            {
                var newNavLayerHeight = currentNavLayerHeight + adjustmentPixels;
                oNavLayer.style.height = "" + newNavLayerHeight + "px";

                oNavBG = getChildOfType(oNavLayer, "DIV", 0);
                if (oNavBG)
                {
                    var currentNavBGHeight = parseInt(oNavBG.style.height);
                    var newNavBGHeight = currentNavBGHeight + adjustmentPixels;
                    oNavBG.style.height = "" + newNavBGHeight + "px";
                }
            }
        }

    }
}
