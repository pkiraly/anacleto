var aFormDefinitions = {
  shelfNode: {
    type: 'NodeType',
    legend: 'Shelf data',
    prefix: 'shelf',
    fields: {
      title: { type: 'text' },
      titlePage: { type: 'text', title: 'title page' }
    }
  },

  bookNode: {
    type: 'NodeType',
    legend: 'Book data',
    prefix: 'book',
    fields: {
      title: { type: 'text' },
      titlepage: { type: 'text', title: 'title page' },
      contentRootUrl: { type: 'text', title: 'content root url' },
      contentHandler: { type: 'text', title: 'content handler' },
      encoding: { type: 'text' },
      scheduled: { 
        type: 'radio', 
        values: [ ['true', 'true'], ['false', 'false', 'checked'] ],
        onchange: function(e){onSchedChange(e, this);}
      },
      schedulingCronExpression: { 
        type: 'text', 
        hide: true,
        label: 'Cron expression'
      },
      contentType: { 
        type: 'select', 
        label: 'content type', 
        onchange: function(e){showSpecialFields(e, this, 'special_container');},
        values: [ 
          ["", '-- Please select a type!'],
          ['FileSystemContent', 'File System Content'],
          ['WebServerContent', 'Web Server Content'],
          ['WebDavContent', 'WebDav Content'],
          ['NXT3CONTENT', 'NXT3 Content'],
          ['PluginContent', 'Plugin Content'],
          ['FTPContent', 'FTP Content', 'disabled'],
          ['MailServerContent', 'Mail Server Content', 'disabled'],
          ['SSHContent', 'SSH Content', 'disabled']
        ]
      }
    }
  },

  FileSystemContent: {
    type: 'contentType',
    legend: 'File System Content',
    prefix: 'fs',
    fields: {
      path:  { type: 'text', title: 'path in filesystem' }
    }
  },

  WebServerContent: {
    type: 'contentType',
    legend: 'Web Server Content',
    prefix: 'web',
    fields: {
      url:  { type: 'text', title: 'URL of the webserver' }
    }
  },

  WebDavContent: {
    type: 'contentType',
    legend: 'WebDav Content',
    prefix: 'webdav',
    fields: {
      path:  { type: 'text', title: 'path in WebDav filesystem' }
    }
  },

  NXT3CONTENT: {
    type: 'contentType',
    legend: 'NXT3 Content',
    prefix: 'nxt3',
    fields: {
      indexsheet: { type: 'text' },
      path:  { type: 'text' },
      nxt3Descriptor: { type: 'text', label: 'NXT3 descriptor' }
    }
  },

  PluginContent: {
    type: 'contentType',
    legend: 'Plugin Content',
    prefix: 'plugin',
    fields: {
      path:  { type: 'text', label: 'path' },
      opt1:  { type: 'text', label: 'Option 1' },
      opt2:  { type: 'text', label: 'Option 2' },
      opt3:  { type: 'text', label: 'Option 3' },
      opt4:  { type: 'text', label: 'Option 4' },
      opt5:  { type: 'text', label: 'Option 5' }
    }
  },

  FTPContent: {
    type: 'contentType',
    legend: 'File Transfer Protocol (FTP) Content',
    prefix: 'ftp',
    fields: {
      path:  { type: 'text', title: 'path in filesystem' },
      username: { type: 'text' },
      password: { type: 'text' }
    }
  },

  MailServerContent: {
    type: 'contentType',
    legend: 'Mail server content',
    prefix: 'mail',
    fields: {
      path:  { type: 'text', label: 'path in filesystem' }
    }
  },

  SSHContent: {
    type: 'contentType',
    legend: 'SSH Content',
    prefix: 'ssh',
    fields: {
      path:  { type: 'text', label: 'path in filesystem' }
    }
  }
};
