from flask import Flask, jsonify, request, render_template, redirect
import logging
from enum import Enum

app = Flask(__name__)
BootupStatus = Enum('BootupStatus', ('Initial', 'NeedBootup'))
current_bootup_status = BootupStatus.Initial

@app.route("/")
def index():
    return render_template('index.html', current_bootup_status = current_bootup_status)

@app.route('/bootup', methods=['POST'])
def bootup():
    global current_bootup_status
    app.logger.info("Need Bootup ! ")
    current_bootup_status = BootupStatus.NeedBootup
    return redirect('/')

@app.route('/get-bootup-status', methods=['GET'])
def get_bootup_status():
    return current_bootup_status.name

@app.route('/has-bootup', methods=['GET'])
def has_bootup():
    app.logger.info("Has Bootup ! ")
    global current_bootup_status
    current_bootup_status = BootupStatus.Initial
    return current_bootup_status.name

if __name__ == '__main__':
    app.run(debug=True)
else:
    gunicorn_logger = logging.getLogger('gunicorn.error')
    app.logger.handlers = gunicorn_logger.handlers
    app.logger.setLevel(gunicorn_logger.level)
