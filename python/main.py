from flask import Flask, request, session, abort, render_template
from werkzeug.utils import redirect
from requests_toolbelt.adapters import appengine
appengine.monkeypatch()
import requests

from usuario.controller import usuario as usuario_controller
from produto.controller import produto as produto_controller
from transacao.controller import transacao as transacao_controller

app = Flask(__name__, template_folder='web')
app.secret_key = 'super secret key'
app.config['SESSION_TYPE'] = 'filesystem'

app.register_blueprint(usuario_controller, url_prefix='/usuario')
app.register_blueprint(produto_controller, url_prefix='/produto')
app.register_blueprint(transacao_controller, url_prefix='/transacao')

@app.route('/')
def hello():
	session['session_state'] = None
	return redirect('https://accounts.google.com/AccountChooser?continue=https://accounts.google.com/o/oauth2/auth?scope%3Dhttps://www.googleapis.com/auth/userinfo.email%26response_type%3Dcode%26redirect_uri%3Dhttp://localhost:8080/token%26state%3Dsecurity_token%253D138r5719ru3e1%2526url%253Dhttps://localhost:8080%26client_id%3D905590247007-tmmromevhmghnve94lc1sqoh08itlhjf.apps.googleusercontent.com%26from_login%3D1%26as%3D-231db6e2ffa9ce49&btmpl=authsub&scc=1&oauth=1')

@app.route('/token')
def token():
    code = request.args.get('code')
    return "got an access token! %s" % get_token(code)


import requests
import requests.auth
def get_token(code):
    client_auth = requests.auth.HTTPBasicAuth('905590247007-tmmromevhmghnve94lc1sqoh08itlhjf.apps.googleusercontent.com', 'l-p5n0NQsBQNOl_naYVxgBFd')
    post_data = {"grant_type": "authorization_code",
                 "code": code,
                 "redirect_uri": 'http://localhost:8080/token'}
    response = requests.post("https://accounts.google.com/o/oauth2/token",
                             auth=client_auth,
                             data=post_data)
    token_json = response.json()
    return user_info(token_json["access_token"])

def user_info(token):
    print '\ntoken\n', token
    access_token = token
    authorization_header = {"Authorization": "OAuth %s" % access_token}
    r = requests.get("https://www.googleapis.com/oauth2/v2/userinfo",
                     headers=authorization_header)

    return r.text


def save_created_state(state):
    pass
def is_valid_state(state):
    return True

@app.errorhandler(404)
def page_not_found(e):
    return 'Sorry, Nothing at this URL.', 404

@app.errorhandler(500)
def application_error(e):
    return 'Sorry, unexpected error: {}'.format(e), 500