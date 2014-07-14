/**
 * fileInput		DOM元素。file控件元素。
 * dragDrop			DOM元素。表拖放感应区域元素。
 * upButton			DOM元素。提交的按钮元素。
 * url				字符串。表示文件ajax上传的地址。
 * fileFilter		过滤后的文件对象数组，一般不参与初始化。可用来判断当前列表的数目。
 * filter			函数。用来过滤选择的文件列表。例如只能是选择图片，或是大小尺寸限制等。支持一个参数(files)，为文件对象数组，需返回数组。
 * onSelect			函数。当本地文件被选择之后执行的回调。支持一个参数(files)，为文件对象数组。
 * onDelete			函数。当某一个上传文件上传成功之后（自动）或被删除（手动）的时候执行的方法。接受一个参数(file)，表当前删除文件。
 * onDragOver		函数。当本地文件被拖到拖拽敏感元素上面时执行的方法。方法中的this指该敏感元素，也就是上面的dragDrop元素。
 * onDragLeave		函数。当本地文件离开拖拽敏感元素时执行的方法。方法中的this指该敏感元素，也就是上面的dragDrop元素。
 * onProgress		函数。文件上传过程中执行的回调方法。接受三个参数(file, loaded, total)，分别表示当前上传文件对象，已上传字节数，文件总字节数。
 * onSuccess		函数。当前文件上传成功执行的回调方法。接受两个参数(file, response)，表示当前上传成功的文件对象和后台返回的字符内容。
 * onFailure		函数。当前文件上传失败执行的回调方法。接受两个参数(file, response)，表示当前上传失败的文件对象和后台返回的字符内容。
 * onComplete		函数。当前文件对象列表全部上传完毕执行的回调方法。无可用参数。
 * funDragHover		方法。文件拖拽相关。非可用API。
 * funGetFiles		方法。获取选择或拖拽文件。非可用API。
 * funDealFiles		方法。对选择文件进行处理。非可用API。
 * funDeleteFile	方法。删除列表中的某个文件。外部可用API，在手动删除某文件时需调用此方法。
 * funUploadFile	方法。文件上传相关。非可用API。
 * init				方法。初始化，主要是一个元素的事件绑定。非可用API。
 */
var html5_upload = {
	fileInput: null,				// html file控件
	dragDrop: null,					// 拖拽敏感区域
	uploadBtn: null,				// 提交按钮
	url: "",						// ajax地址
	fileFilter: [],					// 过滤后的文件数组
	filter: function(files) {		// 选择文件组的过滤方法
		var arrFiles = [];
		for (var i = 0, file; file = files[i]; i++) {
			arrFiles.push(file);
		}
		return arrFiles;
	},
	onSelect: function() {},		// 文件选择后
	onDelete: function() {},		// 文件删除后
	onDragOver: function() {},		// 文件拖拽到敏感区域时
	onDragLeave: function() {},		// 文件离开到敏感区域时
	onBeforeUpload: function() {},  // 文件上传之前时
	onImagesIsNull: function() {},  // 文件上传时如果没有要上传的文件时
	onProgress: function() {},		// 文件上传进度
	onSuccess: function() {},		// 文件上传成功时
	onFailure: function() {},		// 文件上传失败时
	onComplete: function() {},		// 文件全部上传完毕时
	
	/* ------------------------------开发参数和内置方法分界线------------------------------ */
	
	// 文件拖放
	funDragHover: function(e) {
		e.stopPropagation();
		e.preventDefault();
		this[e.type === "dragover"? "onDragOver": "onDragLeave"].call(e.target);
		return this;
	},
	// 获取选择文件，file控件或拖放
	funGetFiles: function(e) {
		// 取消鼠标经过样式
		this.funDragHover(e);
				
		// 获取文件列表对象
		var files = e.target.files || e.dataTransfer.files;
		// 继续添加文件
		this.fileFilter = this.fileFilter.concat(this.filter(files));
		this.funDealFiles();
		return this;
	},
	
	// 选中文件的处理与回调
	funDealFiles: function() {
		for (var i = 0, file; file = this.fileFilter[i]; i++) {
			// 增加唯一索引值
			file.index = i;
		}
		// 执行选择回调
		this.onSelect(this.fileFilter);
		return this;
	},
	
	// 删除对应的文件
	funDeleteFile: function(fileDelete) {
		var arrFile = [];
		for (var i = 0, file; file = this.fileFilter[i]; i++) {
			if (file != fileDelete) {
				arrFile.push(file);
			} else {
				this.onDelete(fileDelete);
			}
		}
		this.fileFilter = arrFile;
		return this;
	},
	
	// 文件上传
	funUploadFile: function() {
		var self = this;
		self.onBeforeUpload();
		if (location.host.indexOf("sitepointstatic") >= 0) {
			// 非站点服务器上运行
			return;
		}
		if (self.fileFilter.length == 0) {
			self.onImagesIsNull();
			return;
		}
		for (var i = 0, file; file = self.fileFilter[i]; i++) {
			(function(file) {
				var xhr = new XMLHttpRequest();
				if (xhr.upload) {
					// 上传中
					xhr.upload.addEventListener("progress", function(e) {
						self.onProgress(file, e.loaded, e.total);
					}, false);

					// 文件上传成功或是失败
					xhr.onreadystatechange = function(e) {
						if (xhr.readyState == 4) {
							if (xhr.status == 200) {
								self.onSuccess(file, xhr.responseText);
								self.funDeleteFile(file);
								if (!self.fileFilter.length) {
									// 全部完毕
									self.onComplete();
								}
							} else {
								self.onFailure(file, xhr.responseText);
							}
						}
					};

					// 开始上传
					xhr.open("POST", self.url, true);
					var formData = new FormData();
					formData.append(self.fileInput.name, file);
					xhr.send(formData);
				}
			})(file);
		}
	},
	
	init: function() {
		var self = this;
		
		if (self.dragDrop) {
			self.dragDrop.addEventListener("dragover", function(e) { self.funDragHover(e); }, false);
			self.dragDrop.addEventListener("dragleave", function(e) { self.funDragHover(e); }, false);
			self.dragDrop.addEventListener("drop", function(e) { self.funGetFiles(e); }, false);
		}
		
		// 文件选择控件选择
		if (self.fileInput) {
			self.fileInput.addEventListener("change", function(e) { self.funGetFiles(e); }, false);
		}
		
		// 上传按钮提交
		if (self.uploadBtn) {
			self.uploadBtn.addEventListener("click", function(e) { self.funUploadFile(e); }, false);
		}
	}
};
