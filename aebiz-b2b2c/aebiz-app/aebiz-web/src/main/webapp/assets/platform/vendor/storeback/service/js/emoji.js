;(function(local_setup) {

/**
 * @global
 * @namespace
 */
function emoji(){}
	/**
	 * The set of images to use for graphical emoji.
	 *
	 * @memberof emoji
	 * @type {string}
	 */
	emoji.img_set = 'apple';
    /**
     * show as '[表情]'
     */
    emoji.showText = false;

	/**
	 * Configuration details for different image sets. This includes a path to a directory containing the
	 * individual images (`path`) and a URL to sprite sheets (`sheet`). All of these images can be found
	 * in the [emoji-data repository]{@link https://github.com/iamcal/emoji-data}. Using a CDN for these
	 * is not a bad idea.
	 *
	 * @memberof emoji
	 * @type {
	 */
	emoji.img_sets = {
		'apple'    : {'path' : '/static/storeback/service/img/img-apple-64/'   , 'sheet' : '/static/storeback/service/img/img-apple-64/sheet_apple_64.png',    'mask' : 1 },
		'google'   : {'path' : '../demo/emoji-data/img-google-64/'  , 'sheet' : '../demo/emoji-data/sheet_google_64.png',   'mask' : 2 },
		'twitter'  : {'path' : '../demo/emoji-data/img-twitter-64/' , 'sheet' : '../demo/emoji-data/sheet_twitter_64.png',  'mask' : 4 },
		'emojione' : {'path' : '../demo/emoji-data/img-emojione-64/', 'sheet' : '../demo/emoji-data/sheet_emojione_64.png', 'mask' : 8 }
	};

	/**
	 * Use a CSS class instead of specifying a sprite or background image for
	 * the span representing the emoticon. This requires a CSS sheet with
	 * emoticon data-uris.
	 *
	 * @memberof emoji
	 * @type bool
	 * @todo document how to build the CSS stylesheet this requires.
	 */
	emoji.use_css_imgs = false;

	/**
	 * Instead of replacing emoticons with the appropriate representations,
	 * replace them with their colon string representation.
	 * @memberof emoji
	 * @type bool
	 */
	emoji.colons_mode = false;
	emoji.text_mode = false;

	/**
	 * If true, sets the "title" property on the span or image that gets
	 * inserted for the emoticon.
	 * @memberof emoji
	 * @type bool
	 */
	emoji.include_title = true;

	/**
	 * If the platform supports native emoticons, use those instead
	 * of the fallbacks.
	 * @memberof emoji
	 * @type bool
	 */
	emoji.allow_native = true;

	/**
	 * Set to true to use CSS sprites instead of individual images on 
	 * platforms that support it.
	 *
	 * @memberof emoji
	 * @type bool
	 */
	emoji.use_sheet = false;

	/**
	 *
	 * Set to true to avoid black & white native Windows emoji being used.
	 *
	 * @memberof emoji
	 * @type bool
	 */
	emoji.avoid_ms_emoji = true;

	// Keeps track of what has been initialized.
	/** @private */
	emoji.inits = {};
	emoji.map = {};

	/**
	 * @memberof emoji
	 * @param {string} str A string potentially containing ascii emoticons
	 * (ie. `:)`)
	 *
	 * @returns {string} A new string with all emoticons in `str`
	 * replaced by a representatation that's supported by the current
	 * environtment.
	 */
	emoji.replace_emoticons = function(str){
		emoji.init_emoticons();
		return str.replace(emoji.rx_emoticons, function(m, $1, $2){
			var val = emoji.map.emoticons[$2];
			return val ? $1+emoji.replacement(val, $2) : m;
		});
	};

	/**
	 * @memberof emoji
	 * @param {string} str A string potentially containing ascii emoticons
	 * (ie. `:)`)
	 *
	 * @returns {string} A new string with all emoticons in `str`
	 * replaced by their colon string representations (ie. `:smile:`)
	 */
	emoji.replace_emoticons_with_colons = function(str){
		emoji.init_emoticons();
		return str.replace(emoji.rx_emoticons, function(m, $1, $2){
			var val = emoji.data[emoji.map.emoticons[$2]][3][0];
			return val ? $1+':'+val+':' : m;
		});
	};

	/**
	 * @memberof emoji
	 * @param {string} str A string potentially containing colon string
	 * representations of emoticons (ie. `:smile:`)
	 *
	 * @returns {string} A new string with all colon string emoticons replaced
	 * with the appropriate representation.
	 */
	emoji.replace_colons = function(str){
		emoji.init_colons();

		return str.replace(emoji.rx_colons, function(m){
			var idx = m.substr(1, m.length-2);

			// special case - an emoji with a skintone modified
			if (idx.indexOf('::skin-tone-') > -1){

				var skin_tone = idx.substr(-1, 1);
				var skin_idx = 'skin-tone-'+skin_tone;
				var skin_val = emoji.map.colons[skin_idx];

				idx = idx.substr(0, idx.length - 13);

				var val = emoji.map.colons[idx];
				if (val){
					return emoji.replacement(val, idx, ':', {
						'idx'		: skin_val,
						'actual'	: skin_idx,
						'wrapper'	: ':'
					});
				}else{
					return ':' + idx + ':' + emoji.replacement(skin_val, skin_idx, ':');
				}
			}else{
				var val = emoji.map.colons[idx];
				return val ? emoji.replacement(val, idx, ':') : m;
			}
		});
	};

	/**
	 * @memberof emoji
	 * @param {string} str A string potentially containing unified unicode
	 * emoticons. (ie. 😄)
	 *
	 * @returns {string} A new string with all unicode emoticons replaced with
	 * the appropriate representation for the current environment.
	 */
	emoji.replace_unified = function(str){
		emoji.init_unified();
		return str.replace(emoji.rx_unified, function(m, p1, p2){
			var val = emoji.map.unified[p1];
			if (!val) return m;
			var idx = null;
			if (p2 == '\uD83C\uDFFB') idx = '1f3fb';
			if (p2 == '\uD83C\uDFFC') idx = '1f3fc';
			if (p2 == '\uD83C\uDFFD') idx = '1f3fd';
			if (p2 == '\uD83C\uDFFE') idx = '1f3fe';
			if (p2 == '\uD83C\uDFFF') idx = '1f3ff';
			if (idx){
				return emoji.replacement(val, null, null, {
					idx	: idx,
					actual	: p2,
					wrapper	: ''
				});
			}
			return emoji.replacement(val);
		});
	};

	// Does the actual replacement of a character with the appropriate
	/** @private */
	emoji.replacement = function(idx, actual, wrapper, variation){

		// for emoji with variation modifiers, set `etxra` to the standalone output for the
		// modifier (used if we can't combine the glyph) and set variation_idx to key of the
		// variation modifier (used below)
		var extra = '';
		var variation_idx = 0;
		if (typeof variation === 'object'){
			extra = emoji.replacement(variation.idx, variation.actual, variation.wrapper);
			variation_idx = idx + '-' + variation.idx;
		}

		// deal with simple modes (colons and text) first
		wrapper = wrapper || '';
		if (emoji.colons_mode) return ':'+emoji.data[idx][3][0]+':'+extra;
		var text_name = (actual) ? wrapper+actual+wrapper : emoji.data[idx][8] || wrapper+emoji.data[idx][3][0]+wrapper;
		if (emoji.text_mode) return text_name + extra;

		// native modes next.
		// for variations selectors, we just need to output them raw, which `extra` will contain.
		emoji.init_env();
		if (emoji.replace_mode == 'unified'  && emoji.allow_native && emoji.data[idx][0][0]) return emoji.data[idx][0][0] + extra;
		if (emoji.replace_mode == 'softbank' && emoji.allow_native && emoji.data[idx][1]) return emoji.data[idx][1] + extra;
		if (emoji.replace_mode == 'google'   && emoji.allow_native && emoji.data[idx][2]) return emoji.data[idx][2] + extra;

		// finally deal with image modes.
		// variation selectors are more complex here - if the image set and particular emoji supports variations, then
		// use the variation image. otherwise, return it as a separate image (already calculated in `extra`).
		// first we set up the params we'll use if we can't use a variation.
		var img = emoji.data[idx][7] || emoji.img_sets[emoji.img_set].path+idx+'.png';
		var title = emoji.include_title ? ' title="'+(actual || emoji.data[idx][3][0])+'"' : '';
		var text  = emoji.include_text  ? wrapper+(actual || emoji.data[idx][3][0])+wrapper : '';
		var px = emoji.data[idx][4];
		var py = emoji.data[idx][5];

		// now we'll see if we can use a varition. if we can, we can override the params above and blank
		// out `extra` so we output a sinlge glyph.
		// we need to check that:
		//  * we requested a variation
		//  * such a variation exists in `emoji.variations_data`
		//  * we're not using a custom image for this glyph
		//  * the variation has an image defined for the current image set
		if (variation_idx && emoji.variations_data[variation_idx] && emoji.variations_data[variation_idx][2] && !emoji.data[idx][9]){
			if (emoji.variations_data[variation_idx][2] & emoji.img_sets[emoji.img_set].mask){
				img = emoji.img_sets[emoji.img_set].path+variation_idx+'.png';
				px = emoji.variations_data[variation_idx][0];
				py = emoji.variations_data[variation_idx][1];
				extra = '';
			}
		}

		if (emoji.supports_css) {
			if (emoji.use_sheet && px != null && py != null){
				var mul = 100 / (emoji.sheet_size - 1);
				var style = 'background: url('+emoji.img_sets[emoji.img_set].sheet+');background-position:'+(mul*px)+'% '+(mul*py)+'%;background-size:'+emoji.sheet_size+'00%';
				return '<span class="emoji-outer emoji-sizer"><span class="emoji-inner" style="'+style+'"'+title+'>'+text+'</span></span>'+extra;
			}else if (emoji.use_css_imgs){
				return '<span class="emoji emoji-'+idx+'"'+title+'>'+text+'</span>'+extra;
			}else if(emoji.showText){
                return '[表情]';
            }else{
				return '<span class="emoji emoji-sizer" style="background-image:url('+img+')"'+title+'>'+text+'</span>'+extra;
			}
		}
		return '<img src="'+img+'" class="emoji" '+title+'/>'+extra;
	};

	// Initializes the text emoticon data
	/** @private */
	emoji.init_emoticons = function(){
		if (emoji.inits.emoticons) return;
		emoji.init_colons(); // we require this for the emoticons map
		emoji.inits.emoticons = 1;
		
		var a = [];
		emoji.map.emoticons = {};
		for (var i in emoji.emoticons_data){
			// because we never see some characters in our text except as entities, we must do some replacing
			var emoticon = i.replace(/\&/g, '&amp;').replace(/\</g, '&lt;').replace(/\>/g, '&gt;');
			
			if (!emoji.map.colons[emoji.emoticons_data[i]]) continue;

			emoji.map.emoticons[emoticon] = emoji.map.colons[emoji.emoticons_data[i]];
			a.push(emoji.escape_rx(emoticon));
		}
		emoji.rx_emoticons = new RegExp(('(^|\\s)('+a.join('|')+')(?=$|[\\s|\\?\\.,!])'), 'g');
	};

	// Initializes the colon string data
	/** @private */
	emoji.init_colons = function(){
		if (emoji.inits.colons) return;
		emoji.inits.colons = 1;
		emoji.rx_colons = new RegExp('\:[a-zA-Z0-9-_+]+\:(\:skin-tone-[2-6]\:)?', 'g');
		emoji.map.colons = {};
		for (var i in emoji.data){
			for (var j=0; j<emoji.data[i][3].length; j++){
				emoji.map.colons[emoji.data[i][3][j]] = i;
			}
		}
	};

	// initializes the unified unicode emoticon data
	/** @private */
	emoji.init_unified = function(){
		if (emoji.inits.unified) return;
		emoji.inits.unified = 1;

		var a = [];
		emoji.map.unified = {};

		for (var i in emoji.data){
			for (var j=0; j<emoji.data[i][0].length; j++){
				a.push(emoji.data[i][0][j]);
				emoji.map.unified[emoji.data[i][0][j]] = i;
			}
		}

		emoji.rx_unified = new RegExp('('+a.join('|')+')(\uD83C[\uDFFB-\uDFFF])?', "g");
	};

	// initializes the environment, figuring out what representation
	// of emoticons is best.
	/** @private */
	emoji.init_env = function(){
		if (emoji.inits.env) return;
		emoji.inits.env = 1;
		emoji.replace_mode = 'img';
		emoji.supports_css = false;
		var ua = navigator.userAgent;
		if (window.getComputedStyle){
			var st = window.getComputedStyle(document.body);
			if (st['background-size'] || st['backgroundSize']){
				emoji.supports_css = true;
			}
		}
		if (ua.match(/(iPhone|iPod|iPad|iPhone\s+Simulator)/i)){
			if (ua.match(/OS\s+[12345]/i)){
				emoji.replace_mode = 'softbank';
				return;
			}
			if (ua.match(/OS\s+[6789]/i)){
				emoji.replace_mode = 'unified';
				return;
			}
		}
		if (ua.match(/Mac OS X 10[._ ](?:[789]|1\d)/i)){
			if (!ua.match(/Chrome/i) && !ua.match(/Firefox/i)){
				emoji.replace_mode = 'unified';
				return;
			}
		}
		if (!emoji.avoid_ms_emoji){
			if (ua.match(/Windows NT 6.[1-9]/i) || ua.match(/Windows NT 10.[0-9]/i)){
				if (!ua.match(/Chrome/i) && !ua.match(/MSIE 8/i)){
					emoji.replace_mode = 'unified';
					return;
				}
			}
		}

		// Need a better way to detect android devices that actually
		// support emoji.
		if (false && ua.match(/Android/i)){
			emoji.replace_mode = 'google';
			return;
		}
		if (emoji.supports_css){
			emoji.replace_mode = 'css';
		}
		// nothing fancy detected - use images
	};
	/** @private */
	emoji.escape_rx = function(text){
		return text.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, "\\$&");
	};
	emoji.sheet_size = 35;
	/** @private */
	emoji.show_data = {
		"1f600":[["\uD83D\uDE00"],"","",["grinning"],25,28,15,0,":D"],
		"1f601":[["\uD83D\uDE01"],"\uE404","\uDBB8\uDF33",["grin"],25,29,15,0],
		"1f602":[["\uD83D\uDE02"],"\uE412","\uDBB8\uDF34",["joy"],25,30,15,0],
		"1f603":[["\uD83D\uDE03"],"\uE057","\uDBB8\uDF30",["smiley"],25,31,15,0,":)"],
		"1f604":[["\uD83D\uDE04"],"\uE415","\uDBB8\uDF38",["smile"],25,32,15,0,":)"],
		"1f605":[["\uD83D\uDE05"],"\uE415\uE331","\uDBB8\uDF31",["sweat_smile"],25,33,15,0],
		"1f606":[["\uD83D\uDE06"],"\uE40A","\uDBB8\uDF32",["laughing","satisfied"],25,34,15,0],
		"1f607":[["\uD83D\uDE07"],"","",["innocent"],26,0,15,0],
		"1f608":[["\uD83D\uDE08"],"","",["smiling_imp"],26,1,15,0],
		"1f609":[["\uD83D\uDE09"],"\uE405","\uDBB8\uDF47",["wink"],26,2,15,0,";)"],
		"1f60a":[["\uD83D\uDE0A"],"\uE056","\uDBB8\uDF35",["blush"],26,3,15,0,":)"],
		"1f60b":[["\uD83D\uDE0B"],"\uE056","\uDBB8\uDF2B",["yum"],26,4,15,0],
		"1f60c":[["\uD83D\uDE0C"],"\uE40A","\uDBB8\uDF3E",["relieved"],26,5,15,0],
		"1f60d":[["\uD83D\uDE0D"],"\uE106","\uDBB8\uDF27",["heart_eyes"],26,6,15,0],
		"1f60e":[["\uD83D\uDE0E"],"","",["sunglasses"],26,7,15,0],
		"1f60f":[["\uD83D\uDE0F"],"\uE402","\uDBB8\uDF43",["smirk"],26,8,15,0],
		"1f610":[["\uD83D\uDE10"],"","",["neutral_face"],26,9,15,0],
		"1f611":[["\uD83D\uDE11"],"","",["expressionless"],26,10,15,0],
		"1f612":[["\uD83D\uDE12"],"\uE40E","\uDBB8\uDF26",["unamused"],26,11,15,0,":("],
		"1f613":[["\uD83D\uDE13"],"\uE108","\uDBB8\uDF44",["sweat"],26,12,15,0],
		"1f614":[["\uD83D\uDE14"],"\uE403","\uDBB8\uDF40",["pensive"],26,13,15,0],
		"1f615":[["\uD83D\uDE15"],"","",["confused"],26,14,15,0],
		"1f616":[["\uD83D\uDE16"],"\uE407","\uDBB8\uDF3F",["confounded"],26,15,15,0],
		"1f617":[["\uD83D\uDE17"],"","",["kissing"],26,16,15,0],
		"1f618":[["\uD83D\uDE18"],"\uE418","\uDBB8\uDF2C",["kissing_heart"],26,17,15,0],
		"1f619":[["\uD83D\uDE19"],"","",["kissing_smiling_eyes"],26,18,15,0],
		"1f61a":[["\uD83D\uDE1A"],"\uE417","\uDBB8\uDF2D",["kissing_closed_eyes"],26,19,15,0],
		"1f61b":[["\uD83D\uDE1B"],"","",["stuck_out_tongue"],26,20,15,0,":p"],
		"1f61c":[["\uD83D\uDE1C"],"\uE105","\uDBB8\uDF29",["stuck_out_tongue_winking_eye"],26,21,15,0,";p"],
		"1f61d":[["\uD83D\uDE1D"],"\uE409","\uDBB8\uDF2A",["stuck_out_tongue_closed_eyes"],26,22,15,0],
		"1f61e":[["\uD83D\uDE1E"],"\uE058","\uDBB8\uDF23",["disappointed"],26,23,15,0,":("],
		"1f61f":[["\uD83D\uDE1F"],"","",["worried"],26,24,15,0],
		"1f620":[["\uD83D\uDE20"],"\uE059","\uDBB8\uDF20",["angry"],26,25,15,0],
		"1f621":[["\uD83D\uDE21"],"\uE416","\uDBB8\uDF3D",["rage"],26,26,15,0],
		"1f622":[["\uD83D\uDE22"],"\uE413","\uDBB8\uDF39",["cry"],26,27,15,0,":'("],
		"1f623":[["\uD83D\uDE23"],"\uE406","\uDBB8\uDF3C",["persevere"],26,28,15,0],
		"1f624":[["\uD83D\uDE24"],"\uE404","\uDBB8\uDF28",["triumph"],26,29,15,0],
		"1f625":[["\uD83D\uDE25"],"\uE401","\uDBB8\uDF45",["disappointed_relieved"],26,30,15,0],
		"1f626":[["\uD83D\uDE26"],"","",["frowning"],26,31,15,0],
		"1f627":[["\uD83D\uDE27"],"","",["anguished"],26,32,15,0],
		"1f628":[["\uD83D\uDE28"],"\uE40B","\uDBB8\uDF3B",["fearful"],26,33,15,0],
		"1f629":[["\uD83D\uDE29"],"\uE403","\uDBB8\uDF21",["weary"],26,34,15,0],
		"1f62a":[["\uD83D\uDE2A"],"\uE408","\uDBB8\uDF42",["sleepy"],27,0,15,0],
		"1f62b":[["\uD83D\uDE2B"],"\uE406","\uDBB8\uDF46",["tired_face"],27,1,15,0],
		"1f62c":[["\uD83D\uDE2C"],"","",["grimacing"],27,2,15,0],
		"1f62d":[["\uD83D\uDE2D"],"\uE411","\uDBB8\uDF3A",["sob"],27,3,15,0,":'("],
		"1f62e":[["\uD83D\uDE2E"],"","",["open_mouth"],27,4,15,0],
		"1f62f":[["\uD83D\uDE2F"],"","",["hushed"],27,5,15,0],
		"1f630":[["\uD83D\uDE30"],"\uE40F","\uDBB8\uDF25",["cold_sweat"],27,6,15,0],
		"1f631":[["\uD83D\uDE31"],"\uE107","\uDBB8\uDF41",["scream"],27,7,15,0],
		"1f632":[["\uD83D\uDE32"],"\uE410","\uDBB8\uDF22",["astonished"],27,8,15,0],
		"1f633":[["\uD83D\uDE33"],"\uE40D","\uDBB8\uDF2F",["flushed"],27,9,15,0],
		"1f634":[["\uD83D\uDE34"],"","",["sleeping"],27,10,15,0],
		"1f635":[["\uD83D\uDE35"],"\uE406","\uDBB8\uDF24",["dizzy_face"],27,11,15,0],
		"1f636":[["\uD83D\uDE36"],"","",["no_mouth"],27,12,15,0],
		"1f637":[["\uD83D\uDE37"],"\uE40C","\uDBB8\uDF2E",["mask"],27,13,15,0],
		"1f638":[["\uD83D\uDE38"],"\uE404","\uDBB8\uDF49",["smile_cat"],27,14,15,0],
		"1f639":[["\uD83D\uDE39"],"\uE412","\uDBB8\uDF4A",["joy_cat"],27,15,15,0],
		"1f63a":[["\uD83D\uDE3A"],"\uE057","\uDBB8\uDF48",["smiley_cat"],27,16,15,0],
		"1f63b":[["\uD83D\uDE3B"],"\uE106","\uDBB8\uDF4C",["heart_eyes_cat"],27,17,15,0],
		"1f63c":[["\uD83D\uDE3C"],"\uE404","\uDBB8\uDF4F",["smirk_cat"],27,18,15,0],
		"1f63d":[["\uD83D\uDE3D"],"\uE418","\uDBB8\uDF4B",["kissing_cat"],27,19,15,0],
		"1f63e":[["\uD83D\uDE3E"],"\uE416","\uDBB8\uDF4E",["pouting_cat"],27,20,15,0],
		"1f63f":[["\uD83D\uDE3F"],"\uE413","\uDBB8\uDF4D",["crying_cat_face"],27,21,15,0],
		"1f640":[["\uD83D\uDE40"],"\uE403","\uDBB8\uDF50",["scream_cat"],27,22,15,0],
		"1f645":[["\uD83D\uDE45"],"\uE423","\uDBB8\uDF51",["no_good"],27,23,15,0],
		"1f646":[["\uD83D\uDE46"],"\uE424","\uDBB8\uDF52",["ok_woman"],27,29,15,0],
		"1f647":[["\uD83D\uDE47"],"\uE426","\uDBB8\uDF53",["bow"],28,0,15,0],
		"1f648":[["\uD83D\uDE48"],"","\uDBB8\uDF54",["see_no_evil"],28,6,15,0],
		"1f649":[["\uD83D\uDE49"],"","\uDBB8\uDF56",["hear_no_evil"],28,7,15,0],
		"1f64a":[["\uD83D\uDE4A"],"","\uDBB8\uDF55",["speak_no_evil"],28,8,15,0],
		"1f64b":[["\uD83D\uDE4B"],"\uE012","\uDBB8\uDF57",["raising_hand"],28,9,15,0],
		"1f64c":[["\uD83D\uDE4C"],"\uE427","\uDBB8\uDF58",["raised_hands"],28,15,15,0],
		"1f64d":[["\uD83D\uDE4D"],"\uE403","\uDBB8\uDF59",["person_frowning"],28,21,15,0],
		"1f64e":[["\uD83D\uDE4E"],"\uE416","\uDBB8\uDF5A",["person_with_pouting_face"],28,27,15,0]
	};
	/** @private */
	emoji.data = {
		"1f600":[["\uD83D\uDE00"],"","",["grinning"],25,28,15,0,":D"],
		"1f601":[["\uD83D\uDE01"],"\uE404","\uDBB8\uDF33",["grin"],25,29,15,0],
		"1f602":[["\uD83D\uDE02"],"\uE412","\uDBB8\uDF34",["joy"],25,30,15,0],
		"1f603":[["\uD83D\uDE03"],"\uE057","\uDBB8\uDF30",["smiley"],25,31,15,0,":)"],
		"1f604":[["\uD83D\uDE04"],"\uE415","\uDBB8\uDF38",["smile"],25,32,15,0,":)"],
		"1f605":[["\uD83D\uDE05"],"\uE415\uE331","\uDBB8\uDF31",["sweat_smile"],25,33,15,0],
		"1f606":[["\uD83D\uDE06"],"\uE40A","\uDBB8\uDF32",["laughing","satisfied"],25,34,15,0],
		"1f607":[["\uD83D\uDE07"],"","",["innocent"],26,0,15,0],
		"1f608":[["\uD83D\uDE08"],"","",["smiling_imp"],26,1,15,0],
		"1f609":[["\uD83D\uDE09"],"\uE405","\uDBB8\uDF47",["wink"],26,2,15,0,";)"],
		"1f60a":[["\uD83D\uDE0A"],"\uE056","\uDBB8\uDF35",["blush"],26,3,15,0,":)"],
		"1f60b":[["\uD83D\uDE0B"],"\uE056","\uDBB8\uDF2B",["yum"],26,4,15,0],
		"1f60c":[["\uD83D\uDE0C"],"\uE40A","\uDBB8\uDF3E",["relieved"],26,5,15,0],
		"1f60d":[["\uD83D\uDE0D"],"\uE106","\uDBB8\uDF27",["heart_eyes"],26,6,15,0],
		"1f60e":[["\uD83D\uDE0E"],"","",["sunglasses"],26,7,15,0],
		"1f60f":[["\uD83D\uDE0F"],"\uE402","\uDBB8\uDF43",["smirk"],26,8,15,0],
		"1f610":[["\uD83D\uDE10"],"","",["neutral_face"],26,9,15,0],
		"1f611":[["\uD83D\uDE11"],"","",["expressionless"],26,10,15,0],
		"1f612":[["\uD83D\uDE12"],"\uE40E","\uDBB8\uDF26",["unamused"],26,11,15,0,":("],
		"1f613":[["\uD83D\uDE13"],"\uE108","\uDBB8\uDF44",["sweat"],26,12,15,0],
		"1f614":[["\uD83D\uDE14"],"\uE403","\uDBB8\uDF40",["pensive"],26,13,15,0],
		"1f615":[["\uD83D\uDE15"],"","",["confused"],26,14,15,0],
		"1f616":[["\uD83D\uDE16"],"\uE407","\uDBB8\uDF3F",["confounded"],26,15,15,0],
		"1f617":[["\uD83D\uDE17"],"","",["kissing"],26,16,15,0],
		"1f618":[["\uD83D\uDE18"],"\uE418","\uDBB8\uDF2C",["kissing_heart"],26,17,15,0],
		"1f619":[["\uD83D\uDE19"],"","",["kissing_smiling_eyes"],26,18,15,0],
		"1f61a":[["\uD83D\uDE1A"],"\uE417","\uDBB8\uDF2D",["kissing_closed_eyes"],26,19,15,0],
		"1f61b":[["\uD83D\uDE1B"],"","",["stuck_out_tongue"],26,20,15,0,":p"],
		"1f61c":[["\uD83D\uDE1C"],"\uE105","\uDBB8\uDF29",["stuck_out_tongue_winking_eye"],26,21,15,0,";p"],
		"1f61d":[["\uD83D\uDE1D"],"\uE409","\uDBB8\uDF2A",["stuck_out_tongue_closed_eyes"],26,22,15,0],
		"1f61e":[["\uD83D\uDE1E"],"\uE058","\uDBB8\uDF23",["disappointed"],26,23,15,0,":("],
		"1f61f":[["\uD83D\uDE1F"],"","",["worried"],26,24,15,0],
		"1f620":[["\uD83D\uDE20"],"\uE059","\uDBB8\uDF20",["angry"],26,25,15,0],
		"1f621":[["\uD83D\uDE21"],"\uE416","\uDBB8\uDF3D",["rage"],26,26,15,0],
		"1f622":[["\uD83D\uDE22"],"\uE413","\uDBB8\uDF39",["cry"],26,27,15,0,":'("],
		"1f623":[["\uD83D\uDE23"],"\uE406","\uDBB8\uDF3C",["persevere"],26,28,15,0],
		"1f624":[["\uD83D\uDE24"],"\uE404","\uDBB8\uDF28",["triumph"],26,29,15,0],
		"1f625":[["\uD83D\uDE25"],"\uE401","\uDBB8\uDF45",["disappointed_relieved"],26,30,15,0],
		"1f626":[["\uD83D\uDE26"],"","",["frowning"],26,31,15,0],
		"1f627":[["\uD83D\uDE27"],"","",["anguished"],26,32,15,0],
		"1f628":[["\uD83D\uDE28"],"\uE40B","\uDBB8\uDF3B",["fearful"],26,33,15,0],
		"1f629":[["\uD83D\uDE29"],"\uE403","\uDBB8\uDF21",["weary"],26,34,15,0],
		"1f62a":[["\uD83D\uDE2A"],"\uE408","\uDBB8\uDF42",["sleepy"],27,0,15,0],
		"1f62b":[["\uD83D\uDE2B"],"\uE406","\uDBB8\uDF46",["tired_face"],27,1,15,0],
		"1f62c":[["\uD83D\uDE2C"],"","",["grimacing"],27,2,15,0],
		"1f62d":[["\uD83D\uDE2D"],"\uE411","\uDBB8\uDF3A",["sob"],27,3,15,0,":'("],
		"1f62e":[["\uD83D\uDE2E"],"","",["open_mouth"],27,4,15,0],
		"1f62f":[["\uD83D\uDE2F"],"","",["hushed"],27,5,15,0],
		"1f630":[["\uD83D\uDE30"],"\uE40F","\uDBB8\uDF25",["cold_sweat"],27,6,15,0],
		"1f631":[["\uD83D\uDE31"],"\uE107","\uDBB8\uDF41",["scream"],27,7,15,0],
		"1f632":[["\uD83D\uDE32"],"\uE410","\uDBB8\uDF22",["astonished"],27,8,15,0],
		"1f633":[["\uD83D\uDE33"],"\uE40D","\uDBB8\uDF2F",["flushed"],27,9,15,0],
		"1f634":[["\uD83D\uDE34"],"","",["sleeping"],27,10,15,0],
		"1f635":[["\uD83D\uDE35"],"\uE406","\uDBB8\uDF24",["dizzy_face"],27,11,15,0],
		"1f636":[["\uD83D\uDE36"],"","",["no_mouth"],27,12,15,0],
		"1f637":[["\uD83D\uDE37"],"\uE40C","\uDBB8\uDF2E",["mask"],27,13,15,0],
		"1f638":[["\uD83D\uDE38"],"\uE404","\uDBB8\uDF49",["smile_cat"],27,14,15,0],
		"1f639":[["\uD83D\uDE39"],"\uE412","\uDBB8\uDF4A",["joy_cat"],27,15,15,0],
		"1f63a":[["\uD83D\uDE3A"],"\uE057","\uDBB8\uDF48",["smiley_cat"],27,16,15,0],
		"1f63b":[["\uD83D\uDE3B"],"\uE106","\uDBB8\uDF4C",["heart_eyes_cat"],27,17,15,0],
		"1f63c":[["\uD83D\uDE3C"],"\uE404","\uDBB8\uDF4F",["smirk_cat"],27,18,15,0],
		"1f63d":[["\uD83D\uDE3D"],"\uE418","\uDBB8\uDF4B",["kissing_cat"],27,19,15,0],
		"1f63e":[["\uD83D\uDE3E"],"\uE416","\uDBB8\uDF4E",["pouting_cat"],27,20,15,0],
		"1f63f":[["\uD83D\uDE3F"],"\uE413","\uDBB8\uDF4D",["crying_cat_face"],27,21,15,0],
		"1f640":[["\uD83D\uDE40"],"\uE403","\uDBB8\uDF50",["scream_cat"],27,22,15,0],
		"1f645":[["\uD83D\uDE45"],"\uE423","\uDBB8\uDF51",["no_good"],27,23,15,0],
		"1f646":[["\uD83D\uDE46"],"\uE424","\uDBB8\uDF52",["ok_woman"],27,29,15,0],
		"1f647":[["\uD83D\uDE47"],"\uE426","\uDBB8\uDF53",["bow"],28,0,15,0],
		"1f648":[["\uD83D\uDE48"],"","\uDBB8\uDF54",["see_no_evil"],28,6,15,0],
		"1f649":[["\uD83D\uDE49"],"","\uDBB8\uDF56",["hear_no_evil"],28,7,15,0],
		"1f64a":[["\uD83D\uDE4A"],"","\uDBB8\uDF55",["speak_no_evil"],28,8,15,0],
		"1f64b":[["\uD83D\uDE4B"],"\uE012","\uDBB8\uDF57",["raising_hand"],28,9,15,0],
		"1f64c":[["\uD83D\uDE4C"],"\uE427","\uDBB8\uDF58",["raised_hands"],28,15,15,0],
		"1f64d":[["\uD83D\uDE4D"],"\uE403","\uDBB8\uDF59",["person_frowning"],28,21,15,0],
		"1f64e":[["\uD83D\uDE4E"],"\uE416","\uDBB8\uDF5A",["person_with_pouting_face"],28,27,15,0]
	};
	/** @private */
	emoji.emoticons_data = {
		"<3":"heart",
		":o)":"monkey_face",
		":*":"kiss",
		":-*":"kiss",
		"<\/3":"broken_heart",
		"=)":"smiley",
		"=-)":"smiley",
		"C:":"smile",
		"c:":"smile",
		":D":"smile",
		":-D":"smile",
		":>":"laughing",
		":->":"laughing",
		";)":"wink",
		";-)":"wink",
		":)":"blush",
		"(:":"blush",
		":-)":"blush",
		"8)":"sunglasses",
		":|":"neutral_face",
		":-|":"neutral_face",
		":\\":"confused",
		":-\\":"confused",
		":\/":"confused",
		":-\/":"confused",
		":p":"stuck_out_tongue",
		":-p":"stuck_out_tongue",
		":P":"stuck_out_tongue",
		":-P":"stuck_out_tongue",
		":b":"stuck_out_tongue",
		":-b":"stuck_out_tongue",
		";p":"stuck_out_tongue_winking_eye",
		";-p":"stuck_out_tongue_winking_eye",
		";b":"stuck_out_tongue_winking_eye",
		";-b":"stuck_out_tongue_winking_eye",
		";P":"stuck_out_tongue_winking_eye",
		";-P":"stuck_out_tongue_winking_eye",
		"):":"disappointed",
		":(":"disappointed",
		":-(":"disappointed",
		">:(":"angry",
		">:-(":"angry",
		":'(":"cry",
		"D:":"anguished",
		":o":"open_mouth",
		":-o":"open_mouth"
	};
	/** @private */
	emoji.variations_data = {
		"261d-1f3fb":[1,3,1],
		"261d-1f3fc":[1,4,1],
		"261d-1f3fd":[1,5,1],
		"261d-1f3fe":[1,6,1],
		"261d-1f3ff":[1,7,1],
		"270a-1f3fb":[2,15,1],
		"270a-1f3fc":[2,16,1],
		"270a-1f3fd":[2,17,1],
		"270a-1f3fe":[2,18,1],
		"270a-1f3ff":[2,19,1],
		"270b-1f3fb":[2,21,1],
		"270b-1f3fc":[2,22,1],
		"270b-1f3fd":[2,23,1],
		"270b-1f3fe":[2,24,1],
		"270b-1f3ff":[2,25,1],
		"270c-1f3fb":[2,27,1],
		"270c-1f3fc":[2,28,1],
		"270c-1f3fd":[2,29,1],
		"270c-1f3fe":[2,30,1],
		"270c-1f3ff":[2,31,1],
		"1f385-1f3fb":[8,4,1],
		"1f385-1f3fc":[8,5,1],
		"1f385-1f3fd":[8,6,1],
		"1f385-1f3fe":[8,7,1],
		"1f385-1f3ff":[8,8,1],
		"1f3c3-1f3fb":[9,24,1],
		"1f3c3-1f3fc":[9,25,1],
		"1f3c3-1f3fd":[9,26,1],
		"1f3c3-1f3fe":[9,27,1],
		"1f3c3-1f3ff":[9,28,1],
		"1f3c4-1f3fb":[9,30,1],
		"1f3c4-1f3fc":[9,31,1],
		"1f3c4-1f3fd":[9,32,1],
		"1f3c4-1f3fe":[9,33,1],
		"1f3c4-1f3ff":[9,34,1],
		"1f3c7-1f3fb":[10,2,1],
		"1f3c7-1f3fc":[10,3,1],
		"1f3c7-1f3fd":[10,4,1],
		"1f3c7-1f3fe":[10,5,1],
		"1f3c7-1f3ff":[10,6,1],
		"1f3ca-1f3fb":[10,10,1],
		"1f3ca-1f3fc":[10,11,1],
		"1f3ca-1f3fd":[10,12,1],
		"1f3ca-1f3fe":[10,13,1],
		"1f3ca-1f3ff":[10,14,1],
		"1f442-1f3fb":[12,32,1],
		"1f442-1f3fc":[12,33,1],
		"1f442-1f3fd":[12,34,1],
		"1f442-1f3fe":[13,0,1],
		"1f442-1f3ff":[13,1,1],
		"1f443-1f3fb":[13,3,1],
		"1f443-1f3fc":[13,4,1],
		"1f443-1f3fd":[13,5,1],
		"1f443-1f3fe":[13,6,1],
		"1f443-1f3ff":[13,7,1],
		"1f446-1f3fb":[13,11,1],
		"1f446-1f3fc":[13,12,1],
		"1f446-1f3fd":[13,13,1],
		"1f446-1f3fe":[13,14,1],
		"1f446-1f3ff":[13,15,1],
		"1f447-1f3fb":[13,17,1],
		"1f447-1f3fc":[13,18,1],
		"1f447-1f3fd":[13,19,1],
		"1f447-1f3fe":[13,20,1],
		"1f447-1f3ff":[13,21,1],
		"1f448-1f3fb":[13,23,1],
		"1f448-1f3fc":[13,24,1],
		"1f448-1f3fd":[13,25,1],
		"1f448-1f3fe":[13,26,1],
		"1f448-1f3ff":[13,27,1],
		"1f449-1f3fb":[13,29,1],
		"1f449-1f3fc":[13,30,1],
		"1f449-1f3fd":[13,31,1],
		"1f449-1f3fe":[13,32,1],
		"1f449-1f3ff":[13,33,1],
		"1f44a-1f3fb":[14,0,1],
		"1f44a-1f3fc":[14,1,1],
		"1f44a-1f3fd":[14,2,1],
		"1f44a-1f3fe":[14,3,1],
		"1f44a-1f3ff":[14,4,1],
		"1f44b-1f3fb":[14,6,1],
		"1f44b-1f3fc":[14,7,1],
		"1f44b-1f3fd":[14,8,1],
		"1f44b-1f3fe":[14,9,1],
		"1f44b-1f3ff":[14,10,1],
		"1f44c-1f3fb":[14,12,1],
		"1f44c-1f3fc":[14,13,1],
		"1f44c-1f3fd":[14,14,1],
		"1f44c-1f3fe":[14,15,1],
		"1f44c-1f3ff":[14,16,1],
		"1f44d-1f3fb":[14,18,1],
		"1f44d-1f3fc":[14,19,1],
		"1f44d-1f3fd":[14,20,1],
		"1f44d-1f3fe":[14,21,1],
		"1f44d-1f3ff":[14,22,1],
		"1f44e-1f3fb":[14,24,1],
		"1f44e-1f3fc":[14,25,1],
		"1f44e-1f3fd":[14,26,1],
		"1f44e-1f3fe":[14,27,1],
		"1f44e-1f3ff":[14,28,1],
		"1f44f-1f3fb":[14,30,1],
		"1f44f-1f3fc":[14,31,1],
		"1f44f-1f3fd":[14,32,1],
		"1f44f-1f3fe":[14,33,1],
		"1f44f-1f3ff":[14,34,1],
		"1f450-1f3fb":[15,1,1],
		"1f450-1f3fc":[15,2,1],
		"1f450-1f3fd":[15,3,1],
		"1f450-1f3fe":[15,4,1],
		"1f450-1f3ff":[15,5,1],
		"1f466-1f3fb":[15,28,1],
		"1f466-1f3fc":[15,29,1],
		"1f466-1f3fd":[15,30,1],
		"1f466-1f3fe":[15,31,1],
		"1f466-1f3ff":[15,32,1],
		"1f467-1f3fb":[15,34,1],
		"1f467-1f3fc":[16,0,1],
		"1f467-1f3fd":[16,1,1],
		"1f467-1f3fe":[16,2,1],
		"1f467-1f3ff":[16,3,1],
		"1f468-1f3fb":[16,5,1],
		"1f468-1f3fc":[16,6,1],
		"1f468-1f3fd":[16,7,1],
		"1f468-1f3fe":[16,8,1],
		"1f468-1f3ff":[16,9,1],
		"1f469-1f3fb":[16,11,1],
		"1f469-1f3fc":[16,12,1],
		"1f469-1f3fd":[16,13,1],
		"1f469-1f3fe":[16,14,1],
		"1f469-1f3ff":[16,15,1],
		"1f46e-1f3fb":[16,21,1],
		"1f46e-1f3fc":[16,22,1],
		"1f46e-1f3fd":[16,23,1],
		"1f46e-1f3fe":[16,24,1],
		"1f46e-1f3ff":[16,25,1],
		"1f470-1f3fb":[16,28,1],
		"1f470-1f3fc":[16,29,1],
		"1f470-1f3fd":[16,30,1],
		"1f470-1f3fe":[16,31,1],
		"1f470-1f3ff":[16,32,1],
		"1f471-1f3fb":[16,34,1],
		"1f471-1f3fc":[17,0,1],
		"1f471-1f3fd":[17,1,1],
		"1f471-1f3fe":[17,2,1],
		"1f471-1f3ff":[17,3,1],
		"1f472-1f3fb":[17,5,1],
		"1f472-1f3fc":[17,6,1],
		"1f472-1f3fd":[17,7,1],
		"1f472-1f3fe":[17,8,1],
		"1f472-1f3ff":[17,9,1],
		"1f473-1f3fb":[17,11,1],
		"1f473-1f3fc":[17,12,1],
		"1f473-1f3fd":[17,13,1],
		"1f473-1f3fe":[17,14,1],
		"1f473-1f3ff":[17,15,1],
		"1f474-1f3fb":[17,17,1],
		"1f474-1f3fc":[17,18,1],
		"1f474-1f3fd":[17,19,1],
		"1f474-1f3fe":[17,20,1],
		"1f474-1f3ff":[17,21,1],
		"1f475-1f3fb":[17,23,1],
		"1f475-1f3fc":[17,24,1],
		"1f475-1f3fd":[17,25,1],
		"1f475-1f3fe":[17,26,1],
		"1f475-1f3ff":[17,27,1],
		"1f476-1f3fb":[17,29,1],
		"1f476-1f3fc":[17,30,1],
		"1f476-1f3fd":[17,31,1],
		"1f476-1f3fe":[17,32,1],
		"1f476-1f3ff":[17,33,1],
		"1f477-1f3fb":[18,0,1],
		"1f477-1f3fc":[18,1,1],
		"1f477-1f3fd":[18,2,1],
		"1f477-1f3fe":[18,3,1],
		"1f477-1f3ff":[18,4,1],
		"1f478-1f3fb":[18,6,1],
		"1f478-1f3fc":[18,7,1],
		"1f478-1f3fd":[18,8,1],
		"1f478-1f3fe":[18,9,1],
		"1f478-1f3ff":[18,10,1],
		"1f47c-1f3fb":[18,15,1],
		"1f47c-1f3fc":[18,16,1],
		"1f47c-1f3fd":[18,17,1],
		"1f47c-1f3fe":[18,18,1],
		"1f47c-1f3ff":[18,19,1],
		"1f481-1f3fb":[18,25,1],
		"1f481-1f3fc":[18,26,1],
		"1f481-1f3fd":[18,27,1],
		"1f481-1f3fe":[18,28,1],
		"1f481-1f3ff":[18,29,1],
		"1f482-1f3fb":[18,31,1],
		"1f482-1f3fc":[18,32,1],
		"1f482-1f3fd":[18,33,1],
		"1f482-1f3fe":[18,34,1],
		"1f482-1f3ff":[19,0,1],
		"1f483-1f3fb":[19,2,1],
		"1f483-1f3fc":[19,3,1],
		"1f483-1f3fd":[19,4,1],
		"1f483-1f3fe":[19,5,1],
		"1f483-1f3ff":[19,6,1],
		"1f485-1f3fb":[19,9,1],
		"1f485-1f3fc":[19,10,1],
		"1f485-1f3fd":[19,11,1],
		"1f485-1f3fe":[19,12,1],
		"1f485-1f3ff":[19,13,1],
		"1f486-1f3fb":[19,15,1],
		"1f486-1f3fc":[19,16,1],
		"1f486-1f3fd":[19,17,1],
		"1f486-1f3fe":[19,18,1],
		"1f486-1f3ff":[19,19,1],
		"1f487-1f3fb":[19,21,1],
		"1f487-1f3fc":[19,22,1],
		"1f487-1f3fd":[19,23,1],
		"1f487-1f3fe":[19,24,1],
		"1f487-1f3ff":[19,25,1],
		"1f4aa-1f3fb":[20,26,1],
		"1f4aa-1f3fc":[20,27,1],
		"1f4aa-1f3fd":[20,28,1],
		"1f4aa-1f3fe":[20,29,1],
		"1f4aa-1f3ff":[20,30,1],
		"1f645-1f3fb":[27,24,1],
		"1f645-1f3fc":[27,25,1],
		"1f645-1f3fd":[27,26,1],
		"1f645-1f3fe":[27,27,1],
		"1f645-1f3ff":[27,28,1],
		"1f646-1f3fb":[27,30,1],
		"1f646-1f3fc":[27,31,1],
		"1f646-1f3fd":[27,32,1],
		"1f646-1f3fe":[27,33,1],
		"1f646-1f3ff":[27,34,1],
		"1f647-1f3fb":[28,1,1],
		"1f647-1f3fc":[28,2,1],
		"1f647-1f3fd":[28,3,1],
		"1f647-1f3fe":[28,4,1],
		"1f647-1f3ff":[28,5,1],
		"1f64b-1f3fb":[28,10,1],
		"1f64b-1f3fc":[28,11,1],
		"1f64b-1f3fd":[28,12,1],
		"1f64b-1f3fe":[28,13,1],
		"1f64b-1f3ff":[28,14,1],
		"1f64c-1f3fb":[28,16,1],
		"1f64c-1f3fc":[28,17,1],
		"1f64c-1f3fd":[28,18,1],
		"1f64c-1f3fe":[28,19,1],
		"1f64c-1f3ff":[28,20,1],
		"1f64d-1f3fb":[28,22,1],
		"1f64d-1f3fc":[28,23,1],
		"1f64d-1f3fd":[28,24,1],
		"1f64d-1f3fe":[28,25,1],
		"1f64d-1f3ff":[28,26,1],
		"1f64e-1f3fb":[28,28,1],
		"1f64e-1f3fc":[28,29,1],
		"1f64e-1f3fd":[28,30,1],
		"1f64e-1f3fe":[28,31,1],
		"1f64e-1f3ff":[28,32,1],
		"1f64f-1f3fb":[28,34,1],
		"1f64f-1f3fc":[29,0,1],
		"1f64f-1f3fd":[29,1,1],
		"1f64f-1f3fe":[29,2,1],
		"1f64f-1f3ff":[29,3,1],
		"1f6a3-1f3fb":[30,5,1],
		"1f6a3-1f3fc":[30,6,1],
		"1f6a3-1f3fd":[30,7,1],
		"1f6a3-1f3fe":[30,8,1],
		"1f6a3-1f3ff":[30,9,1],
		"1f6b4-1f3fb":[30,27,1],
		"1f6b4-1f3fc":[30,28,1],
		"1f6b4-1f3fd":[30,29,1],
		"1f6b4-1f3fe":[30,30,1],
		"1f6b4-1f3ff":[30,31,1],
		"1f6b5-1f3fb":[30,33,1],
		"1f6b5-1f3fc":[30,34,1],
		"1f6b5-1f3fd":[31,0,1],
		"1f6b5-1f3fe":[31,1,1],
		"1f6b5-1f3ff":[31,2,1],
		"1f6b6-1f3fb":[31,4,1],
		"1f6b6-1f3fc":[31,5,1],
		"1f6b6-1f3fd":[31,6,1],
		"1f6b6-1f3fe":[31,7,1],
		"1f6b6-1f3ff":[31,8,1],
		"1f6c0-1f3fb":[31,19,1],
		"1f6c0-1f3fc":[31,20,1],
		"1f6c0-1f3fd":[31,21,1],
		"1f6c0-1f3fe":[31,22,1],
		"1f6c0-1f3ff":[31,23,1]
	};

	if (typeof exports === 'object'){
		module.exports = emoji;
	}else if (typeof define === 'function' && define.amd){
		define(function() { return emoji; });
	}else{
		this.emoji = emoji;
	}
	
	if (local_setup) local_setup(emoji);
}).call(function(){
	return this || (typeof window !== 'undefined' ? window : global);
}(), function(emoji) {
	
	// Set up emoji for your environment here!
	// For instance, you might want to always
	// render emoji as HTML, and include the
	// name as the title of the HTML elements:

	/*
	emoji.include_title = true;
	emoji.allow_native = false;
	*/

	// And you might want to always use
	// Google's emoji images:

	/*
	emoji.img_set = 'google';
	*/

	// And you might want want to point to
	// a CDN for your sheets and img files

	/*
	emoji.img_sets['google']['path'] = 'http://cdn.example.com/emoji/';
	emoji.img_sets['google']['sheet'] = 'http://cdn.example.com/emoji/sheet_google_64.png';
	*/

});
