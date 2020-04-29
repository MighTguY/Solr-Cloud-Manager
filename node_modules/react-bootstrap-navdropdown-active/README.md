[![Build Status](https://travis-ci.org/neomusic/react-bootstrap-navdropdown-active.svg?branch=master)](https://travis-ci.org/neomusic/react-bootstrap-navdropdown-active)
[![npm version](https://badge.fury.io/js/react-bootstrap-navdropdown-active.svg)](https://badge.fury.io/js/react-bootstrap-navdropdown-active)

# React Bootstrap Navdropdown Active

Make Bootstrap dropdown element for your navbar with active class on dropdown element

## Getting Started

### Prerequisites

`react`: `^15.4.1`

### Installing

`npm install --save react-bootstrap-navdropdown-active`

### Usage 

```
import ActiveDropdown from ‘react-bootstrap-navdropdown-active’

<ActiveDropdown activeRoutes={['/foo/bar', ‘/bar/baz’]} baseClassName="dropdown-toggle" title="Foo">
	<li><Link to=“/foo/bar” activeClassName=“active”>Foo</Link></li>
	<li><Link to=“/bar/baz” activeClassName=“active”>Baz</Link></li>
</ActiveDropdown>          

```

**Result**

```
<li class="dropdown">
  <a class=“dropdown-toogle” data-toggle=“dropdown” role=“button” aria-haspopup=“true” aria-expanded=“false”>Foo<span className=“caret” /></a>
    <ul className=“dropdown-menu”>
      <li><a href=“/foo/bar”>Foo</a></li>
      <li><a href="/bar/baz“>Bar</a></li>
    </ul>
</li>
```

### Parameters

| Name Parameter | Required | Type | Note |
| --- | --- | --- | --- |
| `activeRoutes ` | Yes  | Array  | Use this parameter to define which routes need to compare  |   |
|  `baseClassName ` |  Yes | String  | Set the default CSS class independently for active class. If nothing CSS class must be used by default set empty string  |   |
|  `title ` | Yes  | String  | Set the name showed on top of dropdown  |   |



## Running the tests

`npm test`


## Contributing

* `Fork the repo`
* `Make tests for your contribution`
* `Make you contribute`
* ` Running tests`
* `Make a pull request`

## Authors

* **Giuseppe Aremare** - *Initial work* - [Twitter](https://twitter.com/beppeA95)  [GitHub](https://github.com/neomusic)
