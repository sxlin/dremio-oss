/*
 * Copyright (C) 2017-2019 Dremio Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import { PureComponent } from "react";

import PropTypes from "prop-types";

import MenuList from "@material-ui/core/MenuList";

class ExploreMenu extends PureComponent {
  static propTypes = {
    children: PropTypes.node,
  };

  render() {
    return <MenuList style={styles.base}>{this.props.children}</MenuList>;
  }
}

const styles = {
  base: {
    position: "relative",
    minWidth: 110,
    paddingTop: 4,
    paddingBottom: 4,
  },
};

export default ExploreMenu;
